port module Main exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Http exposing (..)
import RemoteData exposing (..)
import DataPointCodec exposing (..)
import Json.Decode exposing (decodeString)
import Maybe.Extra as MaybeExtra
import Date.Extra.Format as DateFormat
import Date.Extra.Config.Config_en_gb exposing (config)
import Dict
import Base64

--TODO: image(s)!
--TODO: args
--TODO: improve in_ rendering (needs better json)
--TODO: improve bys rendering (less map like)
--TODO: show just the time element
--TODO: maybe render failures on one line and quoted
--TODO: render ticks and cross instead of whole line green/red
--TODO: render each 'by' as a pill
--TODO: render 'in' and 'expected' in grey to de-emphasis
--TODO: share styles
--TODO: do something more clever with spaced, build a list of Html and set all their styles
--TODO: consider expected before in
--TODO: connect screenshot with it's datapoint
--TODO: for first cut maybe just have screenshot.png

--outstanding:
--rendering of actual for expectedMany is not listy ... expected: ["foo"] actual: "bar"
--inflightAnnouncement needs isError and render in red, ideally set Context(success=Some(false))
--better rendering of in. shorten to just Body etc
--possibly fold in into by's
--make images be base64 encoded into the page
--maybe make js inline
--can we snapshot the image and or html of the element under assertion (inline even better)
--ensure we can handling quotes in messages ...
--embolden the > in bys
--consider timestamp filenames again to avoid windows logging

type alias Model =
    { raw : String
    , dataPoints : List DataPoint
    , error : Maybe String
    }


type Msg
    = LoadData String
    | ParseData


init : ( Model, Cmd Msg )
init =
    ( Model "" [] Nothing, Cmd.none )


view : Model -> Html Msg
view model =
    let
        isError = MaybeExtra.isJust model.error
    in
        div []
            [ if isError then div [] [text (model.error |> Maybe.withDefault "") ] else nowt
            , div [] (List.map (\dp -> renderDataPoint dp )model.dataPoints)
            , if isError then div [] [
                hr [] []
                , text ("raw:" ++ toString model.raw)
                ]
                else nowt
            ]

renderDataPoint : DataPoint -> Html msg
renderDataPoint dataPoint =
    let
        color = case dataPoint.context of
                    Nothing -> "grey"
                    Just context -> case context.success of
                        Nothing -> "#cccc00"
                        Just success -> if success then "#00cc00" else "#cc0000"
    in
        div [ style [ ("min-height", "20px"), ("padding-bottom", "5px") ] ] [
            span [ ] [
                span [style [ gapRight, smaller, grey ]] [text (DateFormat.format config "%H:%M:%S.%L" dataPoint.when)]
                , span [style [ ("color", color), ("font-weight", "bold"), gapRight ]] [ text "*"]
                , if MaybeExtra.isJust dataPoint.description then span [style [ gapRight, smaller ] ] [text (dataPoint.description |> Maybe.withDefault "") ] else nowt
                , renderCommand dataPoint.command
                , renderContext dataPoint.context
            ]
        ]

gapRight : (String, String)
gapRight = ("margin-right", "6px")

grey : (String, String)
grey = ("color", "grey")

smaller : (String, String)
smaller = ("font-size", "smaller")

renderCommand : Maybe Command -> Html msg
renderCommand maybeCommand =
    case maybeCommand of
        Nothing -> nowt
        Just command -> span [] [
            span [style [ gapRight, smaller ]] [text command.name]
            , span [style [smaller]] [renderBys command.bys]
            , span [style [smaller]] [renderArgs command.args]
            , span [style [smaller]] [renderIn command.in_]
            , renderExpected command.expected command.expectedMany
            --TODO: whack me when done ....
--            , div [style [ ("margin-right", "7px")]] [ text (toString command) ]
            ]

--class "lozenge",

renderExpected : Maybe String -> Maybe (List String) -> Html msg
renderExpected expected expectedMany =
    case (expected, expectedMany) of
        (Just e, _) -> span [style [ gapRight, smaller ]] [ span [style [ gapRight ]] [span [style [smaller, grey ]] [text "expected:"] ], text ("\"" ++ e ++ "\"") ]
        (_, Just me) -> span [style [ gapRight, smaller ]] [ span [style [ gapRight ]] [span [style [smaller, grey] ] [text "expected:"] ], text (toString me) ]
        (_, _) -> nowt

renderArgs : Dict.Dict String String -> Html msg
renderArgs args =
    if Dict.isEmpty args then nowt else span [class "lozengex" ,style [ smaller, gapRight ]]
      ((List.map (\k -> renderArg k (Dict.get k args)) (Dict.keys args)) |> List.intersperse (span [style [ smaller ]] [text ",  "]))

renderArg : String -> Maybe String -> Html msg
renderArg k v =
    let
      (key, value) = (k, v |> Maybe.withDefault "???" )
    in
      span [] [span [style [ smaller, grey ]] [text (key ++ ": ")], span [] [text value]]


renderBys : List (List (String, String)) -> Html msg
renderBys bys =
    if List.isEmpty bys then nowt else span [class "lozengex" , style [ smaller, gapRight ]] [
      span [ style [ grey] ] [ text "(" ]
      , span [] ((List.map (\b -> renderBy b) bys) |> List.intersperse (span [style [ smaller ]] [text " > "]))
      , span [ style [ grey] ] [ text ")" ]
      ]


renderBy : List (String, String) -> Html msg
renderBy by =
    let
      (key, value) = List.head by |> Maybe.withDefault ("???", "???")
    in
      span [] [span [style [ smaller, grey ]] [text (key ++ ": ")], span [] [text value]]

renderIn : Maybe String -> Html msg
renderIn maybeIn =
    case maybeIn of
        Nothing -> nowt
        Just in_ -> span [style [ gapRight, smaller ]] [ span [style [ gapRight, grey, smaller ]] [text "in:" ], text in_ ]

renderContext : Maybe Context -> Html msg
renderContext maybeContext =
    case maybeContext of
        Nothing -> nowt
        Just context -> if not (List.isEmpty context.failures) && not (case context.success of
                                                                                     Nothing -> False
                                                                                     Just success -> success) then
--                        else span [] [text "actual: ", span [] ((List.map (\f -> span [] [text f]) context.failures) |> List.intersperse (span [style [ smaller ]] [text ", "]))]
--                        span [style [smaller]] [span [style [grey, smaller]] [ text "actual: "], span [] ((List.map (\f -> span [] [text ("\"" ++ f ++ "\"")]) context.failures) |> List.intersperse (span [style [ smaller ]] [text ", "]))]
                        span [style [smaller]] [span [style [grey, smaller]] [ text "actual: "], span [ style [ ("color", "#cc0000") ]] [text (List.reverse context.failures |> List.head |> Maybe.map (\f -> "\"" ++ f ++ "\"") |> Maybe.withDefault "???" )] ]
                        else nowt

nowt : Html msg
nowt =
    text ""

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        LoadData data ->
            let
                d = Debug.log "b64" (toString (Base64.decode "W3sKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny4zNjdaIiwKICAiZGVzY3JpcHRpb24iOiJhbm5vdW5jZW1lbnQiCn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjYyNVoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBleHBlY3RlZCIsCiAgICAiYnlzIjpbXSwKICAgICJhcmdzIjp7CiAgICAgIAogICAgfSwKICAgICJleHBlY3RlZCI6ImV4cGVjdGVkIgogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6W10KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3Ljc4MloiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBleHBlY3RlZCBtYW55IiwKICAgICJieXMiOltdLAogICAgImFyZ3MiOnsKICAgICAgCiAgICB9LAogICAgImV4cGVjdGVkTWFueSI6WyJleHBlY3RlZCIsImV4cGVjdGVkMiJdCiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbXQogIH0KfSx7CiAgImZsaWdodE51bWJlciI6MSwKICAid2hlbiI6IjIwMTctMDgtMDJUMTc6NDg6NDcuNzg0WiIsCiAgImNvbW1hbmQiOnsKICAgICJuYW1lIjoiY29tbWFuZCB3aXRoIGluIiwKICAgICJpbiI6Iltvcmcub3BlbnFhLnNlbGVuaXVtLnJlbW90ZS5SZW1vdGVXZWJFbGVtZW50QGYwOTc1OTM3IC0+IHVua25vd24gbG9jYXRvcl0iLAogICAgImJ5cyI6W10sCiAgICAiYXJncyI6ewogICAgICAKICAgIH0KICB9LAogICJjb250ZXh0Ijp7CiAgICAiZmFpbHVyZXMiOltdCiAgfQp9LHsKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny44MThaIiwKICAiY29tbWFuZCI6ewogICAgIm5hbWUiOiJjb21tYW5kIHdpdGggYnkiLAogICAgImJ5cyI6W3sKICAgICAgImlkIjoiaWQiCiAgICB9XSwKICAgICJhcmdzIjp7CiAgICAgIAogICAgfQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6W10KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjgyMVoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBieSBwYXRoIiwKICAgICJieXMiOlt7CiAgICAgICJpZCI6ImlkIgogICAgfSx7CiAgICAgICJjbGFzc05hbWUiOiJjbGFzcyIKICAgIH1dLAogICAgImFyZ3MiOnsKICAgICAgCiAgICB9CiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbXQogIH0KfSx7CiAgImZsaWdodE51bWJlciI6MSwKICAid2hlbiI6IjIwMTctMDgtMDJUMTc6NDg6NDcuODc0WiIsCiAgImNvbW1hbmQiOnsKICAgICJuYW1lIjoiY29tbWFuZCB3aXRoIGJ5IHBhdGggbmVzdGVkIiwKICAgICJieXMiOlt7CiAgICAgICJpZCI6ImlkIgogICAgfSx7CiAgICAgICJjbGFzc05hbWUiOiJjbGFzcyIKICAgIH0sewogICAgICAieHBhdGhFeHByZXNzaW9uIjoieHBhdGgiCiAgICB9LHsKICAgICAgInNlbGVjdG9yIjoiY3NzU2VsZWN0b3IiCiAgICB9XSwKICAgICJhcmdzIjp7CiAgICAgIAogICAgfQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6W10KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzNVoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBhcmdzIiwKICAgICJieXMiOltdLAogICAgImFyZ3MiOnsKICAgICAgImtleSI6InZhbHVlIgogICAgfQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6W10KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzNVoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBjb250ZXh0IHRydWUiLAogICAgImJ5cyI6W10sCiAgICAiYXJncyI6ewogICAgICAKICAgIH0KICB9LAogICJjb250ZXh0Ijp7CiAgICAiZmFpbHVyZXMiOltdLAogICAgInN1Y2Nlc3MiOiJ0cnVlIgogIH0KfSx7CiAgImZsaWdodE51bWJlciI6MSwKICAid2hlbiI6IjIwMTctMDgtMDJUMTc6NDg6NDcuOTM2WiIsCiAgImNvbW1hbmQiOnsKICAgICJuYW1lIjoiY29tbWFuZCB3aXRoIGNvbnRleHQgZmFsc2UiLAogICAgImJ5cyI6W10sCiAgICAiYXJncyI6ewogICAgICAKICAgIH0KICB9LAogICJjb250ZXh0Ijp7CiAgICAiZmFpbHVyZXMiOltdLAogICAgInN1Y2Nlc3MiOiJmYWxzZSIKICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzNloiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBjb250ZXh0IGZhaWx1cmVzIiwKICAgICJieXMiOltdLAogICAgImFyZ3MiOnsKICAgICAgCiAgICB9CiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbImZhaWx1cmVzIl0KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzN1oiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImV2ZXJ5dGhpbmciLAogICAgImluIjoiW29yZy5vcGVucWEuc2VsZW5pdW0ucmVtb3RlLlJlbW90ZVdlYkVsZW1lbnRAZjA5NzU5MzcgLT4gdW5rbm93biBsb2NhdG9yXSIsCiAgICAiYnlzIjpbewogICAgICAiaWQiOiJpZCIKICAgIH1dLAogICAgImFyZ3MiOnsKICAgICAgImtleSI6InZhbHVlIgogICAgfSwKICAgICJleHBlY3RlZCI6ImV4cGVjdGVkIiwKICAgICJleHBlY3RlZE1hbnkiOlsiZXhwZWN0ZWQiLCJleHBlY3RlZDIiXQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6WyJmYWlsdXJlcyJdLAogICAgInN1Y2Nlc3MiOiJmYWxzZSIKICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzN1oiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImV2ZXJ5dGhpbmcgd2l0aCBwYXRoIiwKICAgICJpbiI6Iltvcmcub3BlbnFhLnNlbGVuaXVtLnJlbW90ZS5SZW1vdGVXZWJFbGVtZW50QGYwOTc1OTM3IC0+IHVua25vd24gbG9jYXRvcl0iLAogICAgImJ5cyI6W3sKICAgICAgImlkIjoiaWQiCiAgICB9XSwKICAgICJhcmdzIjp7CiAgICAgICJrZXkiOiJ2YWx1ZSIKICAgIH0sCiAgICAiZXhwZWN0ZWQiOiJleHBlY3RlZCIsCiAgICAiZXhwZWN0ZWRNYW55IjpbImV4cGVjdGVkIiwiZXhwZWN0ZWQyIl0KICB9LAogICJjb250ZXh0Ijp7CiAgICAiZmFpbHVyZXMiOlsiZmFpbHVyZXMiXSwKICAgICJzdWNjZXNzIjoiZmFsc2UiCiAgfQp9LHsKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny45MzhaIiwKICAiY29tbWFuZCI6ewogICAgIm5hbWUiOiJlc2NhcGluZyIsCiAgICAiaW4iOiJbb3JnLm9wZW5xYS5zZWxlbml1bS5yZW1vdGUuUmVtb3RlV2ViRWxlbWVudEBmMDk3NTkzNyAtPiB1bmtub3duIGxvY2F0b3JdIiwKICAgICJieXMiOlt7CiAgICAgICJpZCI6ImlkIgogICAgfV0sCiAgICAiYXJncyI6ewogICAgICAKICAgIH0sCiAgICAiZXhwZWN0ZWRNYW55IjpbIlwiZXhwZWN0ZWRcIiJdCiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbImZhaWx1cmUgXCIxXCIiLCInMiciXSwKICAgICJzdWNjZXNzIjoiZmFsc2UiCiAgfQp9LHsKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny45MzhaIiwKICAiY29tbWFuZCI6ewogICAgIm5hbWUiOiJHb3RvIiwKICAgICJieXMiOltdLAogICAgImFyZ3MiOnsKICAgICAgInVybCI6Imh0dHA6Ly9mb28uYmFyLmNvbS9iYXoiCiAgICB9CiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbXSwKICAgICJzdWNjZXNzIjoidHJ1ZSIKICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzOFoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6IkNsaWNrIiwKICAgICJpbiI6Iltvcmcub3BlbnFhLnNlbGVuaXVtLnJlbW90ZS5SZW1vdGVXZWJFbGVtZW50QGYwOTc1OTM3IC0+IHVua25vd24gbG9jYXRvcl0iLAogICAgImJ5cyI6W3sKICAgICAgImlkIjoiaWQiCiAgICB9XSwKICAgICJhcmdzIjp7CiAgICAgIAogICAgfQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6WyJmYWlsZWQgZm9yIGEgYml0Il0sCiAgICAic3VjY2VzcyI6InRydWUiCiAgfQp9LHsKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny45MzlaIiwKICAiY29tbWFuZCI6ewogICAgIm5hbWUiOiJBc3NlcnRFbGVtZW50TGlzdFRleHRFcXVhbHMiLAogICAgImluIjoiW29yZy5vcGVucWEuc2VsZW5pdW0ucmVtb3RlLlJlbW90ZVdlYkVsZW1lbnRAZjA5NzU5MzcgLT4gdW5rbm93biBsb2NhdG9yXSIsCiAgICAiYnlzIjpbewogICAgICAiaWQiOiJpZCIKICAgIH1dLAogICAgImFyZ3MiOnsKICAgICAgCiAgICB9LAogICAgImV4cGVjdGVkTWFueSI6WyJleHBlY3RlZCJdCiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbImZhaWx1cmVzIl0sCiAgICAic3VjY2VzcyI6ImZhbHNlIgogIH0KfV0="))
            in
                update ParseData { model | raw = data }

        ParseData ->
            let
                result = (Json.Decode.decodeString decodeDataPointList model.raw)
                model_ = case result of
                    Ok x -> {model | dataPoints = x}
                    Err e -> {model | error = Just e }
            in
            ( model_, Cmd.none )


port data : (String -> msg) -> Sub msg

subscriptions : Model -> Sub Msg
subscriptions model =
  data LoadData


main : Program Never Model Msg
main =
    program
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }
