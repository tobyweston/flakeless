port module AllFlightsReport exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import ViewShared exposing (..)
import Http exposing (..)
import RemoteData exposing (..)
import AllFlightsReportCodec exposing (..)
import Json.Decode exposing (decodeString)
import Maybe.Extra as MaybeExtra
import Date.Extra.Format as DateFormat
import Date.Extra.Config.Config_en_gb exposing (config)
import Dict
import Base64


--TODO: link through to reports (if exist)
--TODO: split out suite and test
--TODO: use http://package.elm-lang.org/packages/evancz/elm-sortable-table/1.0.1

type alias Model =
    { raw : String
    , investigations : List Investigation
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
            , ul [] (List.map (\i -> renderInvestigation i ) model.investigations)
            , if isError then div [] [
                hr [] []
                , text ("raw:" ++ toString model.raw)
                ]
                else nowt
            ]

renderInvestigation : Investigation -> Html msg
renderInvestigation investigation =
--    let
--        colorClass = case dataPoint.context of
--                    Nothing -> "message"
--                    Just context -> case context.success of
--                        Nothing -> "dunno"
--                        Just success -> if success then "pass" else "fail"
--    in
--        li [ class colorClass, style [ ("min-height", "20px"), ("padding-bottom", "5px") ] ] [
--            span [ ] [
--                span [style [ gapRight, smaller, grey ]] [text (DateFormat.format config "%H:%M:%S.%L" dataPoint.when)]
----                , span [style [ ("color", colorClass), ("font-weight", "bold"), gapRight ]] [ text "*"]
--                , if MaybeExtra.isJust dataPoint.description then span [style [ gapRight, smaller ] ] [text (dataPoint.description |> Maybe.withDefault "") ] else nowt
--                , renderCommand dataPoint.command
--                , renderContext dataPoint.context
--                , renderLog dataPoint.log
--            ]
--        ]
    div [] [text (toString investigation)]

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        LoadData data ->
            let
                result = Base64.decode data

--                d = Debug.log "b64" (toString (Base64.decode "W3sKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny4zNjdaIiwKICAiZGVzY3JpcHRpb24iOiJhbm5vdW5jZW1lbnQiCn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjYyNVoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBleHBlY3RlZCIsCiAgICAiYnlzIjpbXSwKICAgICJhcmdzIjp7CiAgICAgIAogICAgfSwKICAgICJleHBlY3RlZCI6ImV4cGVjdGVkIgogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6W10KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3Ljc4MloiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBleHBlY3RlZCBtYW55IiwKICAgICJieXMiOltdLAogICAgImFyZ3MiOnsKICAgICAgCiAgICB9LAogICAgImV4cGVjdGVkTWFueSI6WyJleHBlY3RlZCIsImV4cGVjdGVkMiJdCiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbXQogIH0KfSx7CiAgImZsaWdodE51bWJlciI6MSwKICAid2hlbiI6IjIwMTctMDgtMDJUMTc6NDg6NDcuNzg0WiIsCiAgImNvbW1hbmQiOnsKICAgICJuYW1lIjoiY29tbWFuZCB3aXRoIGluIiwKICAgICJpbiI6Iltvcmcub3BlbnFhLnNlbGVuaXVtLnJlbW90ZS5SZW1vdGVXZWJFbGVtZW50QGYwOTc1OTM3IC0+IHVua25vd24gbG9jYXRvcl0iLAogICAgImJ5cyI6W10sCiAgICAiYXJncyI6ewogICAgICAKICAgIH0KICB9LAogICJjb250ZXh0Ijp7CiAgICAiZmFpbHVyZXMiOltdCiAgfQp9LHsKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny44MThaIiwKICAiY29tbWFuZCI6ewogICAgIm5hbWUiOiJjb21tYW5kIHdpdGggYnkiLAogICAgImJ5cyI6W3sKICAgICAgImlkIjoiaWQiCiAgICB9XSwKICAgICJhcmdzIjp7CiAgICAgIAogICAgfQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6W10KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjgyMVoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBieSBwYXRoIiwKICAgICJieXMiOlt7CiAgICAgICJpZCI6ImlkIgogICAgfSx7CiAgICAgICJjbGFzc05hbWUiOiJjbGFzcyIKICAgIH1dLAogICAgImFyZ3MiOnsKICAgICAgCiAgICB9CiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbXQogIH0KfSx7CiAgImZsaWdodE51bWJlciI6MSwKICAid2hlbiI6IjIwMTctMDgtMDJUMTc6NDg6NDcuODc0WiIsCiAgImNvbW1hbmQiOnsKICAgICJuYW1lIjoiY29tbWFuZCB3aXRoIGJ5IHBhdGggbmVzdGVkIiwKICAgICJieXMiOlt7CiAgICAgICJpZCI6ImlkIgogICAgfSx7CiAgICAgICJjbGFzc05hbWUiOiJjbGFzcyIKICAgIH0sewogICAgICAieHBhdGhFeHByZXNzaW9uIjoieHBhdGgiCiAgICB9LHsKICAgICAgInNlbGVjdG9yIjoiY3NzU2VsZWN0b3IiCiAgICB9XSwKICAgICJhcmdzIjp7CiAgICAgIAogICAgfQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6W10KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzNVoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBhcmdzIiwKICAgICJieXMiOltdLAogICAgImFyZ3MiOnsKICAgICAgImtleSI6InZhbHVlIgogICAgfQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6W10KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzNVoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBjb250ZXh0IHRydWUiLAogICAgImJ5cyI6W10sCiAgICAiYXJncyI6ewogICAgICAKICAgIH0KICB9LAogICJjb250ZXh0Ijp7CiAgICAiZmFpbHVyZXMiOltdLAogICAgInN1Y2Nlc3MiOiJ0cnVlIgogIH0KfSx7CiAgImZsaWdodE51bWJlciI6MSwKICAid2hlbiI6IjIwMTctMDgtMDJUMTc6NDg6NDcuOTM2WiIsCiAgImNvbW1hbmQiOnsKICAgICJuYW1lIjoiY29tbWFuZCB3aXRoIGNvbnRleHQgZmFsc2UiLAogICAgImJ5cyI6W10sCiAgICAiYXJncyI6ewogICAgICAKICAgIH0KICB9LAogICJjb250ZXh0Ijp7CiAgICAiZmFpbHVyZXMiOltdLAogICAgInN1Y2Nlc3MiOiJmYWxzZSIKICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzNloiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImNvbW1hbmQgd2l0aCBjb250ZXh0IGZhaWx1cmVzIiwKICAgICJieXMiOltdLAogICAgImFyZ3MiOnsKICAgICAgCiAgICB9CiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbImZhaWx1cmVzIl0KICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzN1oiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImV2ZXJ5dGhpbmciLAogICAgImluIjoiW29yZy5vcGVucWEuc2VsZW5pdW0ucmVtb3RlLlJlbW90ZVdlYkVsZW1lbnRAZjA5NzU5MzcgLT4gdW5rbm93biBsb2NhdG9yXSIsCiAgICAiYnlzIjpbewogICAgICAiaWQiOiJpZCIKICAgIH1dLAogICAgImFyZ3MiOnsKICAgICAgImtleSI6InZhbHVlIgogICAgfSwKICAgICJleHBlY3RlZCI6ImV4cGVjdGVkIiwKICAgICJleHBlY3RlZE1hbnkiOlsiZXhwZWN0ZWQiLCJleHBlY3RlZDIiXQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6WyJmYWlsdXJlcyJdLAogICAgInN1Y2Nlc3MiOiJmYWxzZSIKICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzN1oiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6ImV2ZXJ5dGhpbmcgd2l0aCBwYXRoIiwKICAgICJpbiI6Iltvcmcub3BlbnFhLnNlbGVuaXVtLnJlbW90ZS5SZW1vdGVXZWJFbGVtZW50QGYwOTc1OTM3IC0+IHVua25vd24gbG9jYXRvcl0iLAogICAgImJ5cyI6W3sKICAgICAgImlkIjoiaWQiCiAgICB9XSwKICAgICJhcmdzIjp7CiAgICAgICJrZXkiOiJ2YWx1ZSIKICAgIH0sCiAgICAiZXhwZWN0ZWQiOiJleHBlY3RlZCIsCiAgICAiZXhwZWN0ZWRNYW55IjpbImV4cGVjdGVkIiwiZXhwZWN0ZWQyIl0KICB9LAogICJjb250ZXh0Ijp7CiAgICAiZmFpbHVyZXMiOlsiZmFpbHVyZXMiXSwKICAgICJzdWNjZXNzIjoiZmFsc2UiCiAgfQp9LHsKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny45MzhaIiwKICAiY29tbWFuZCI6ewogICAgIm5hbWUiOiJlc2NhcGluZyIsCiAgICAiaW4iOiJbb3JnLm9wZW5xYS5zZWxlbml1bS5yZW1vdGUuUmVtb3RlV2ViRWxlbWVudEBmMDk3NTkzNyAtPiB1bmtub3duIGxvY2F0b3JdIiwKICAgICJieXMiOlt7CiAgICAgICJpZCI6ImlkIgogICAgfV0sCiAgICAiYXJncyI6ewogICAgICAKICAgIH0sCiAgICAiZXhwZWN0ZWRNYW55IjpbIlwiZXhwZWN0ZWRcIiJdCiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbImZhaWx1cmUgXCIxXCIiLCInMiciXSwKICAgICJzdWNjZXNzIjoiZmFsc2UiCiAgfQp9LHsKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny45MzhaIiwKICAiY29tbWFuZCI6ewogICAgIm5hbWUiOiJHb3RvIiwKICAgICJieXMiOltdLAogICAgImFyZ3MiOnsKICAgICAgInVybCI6Imh0dHA6Ly9mb28uYmFyLmNvbS9iYXoiCiAgICB9CiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbXSwKICAgICJzdWNjZXNzIjoidHJ1ZSIKICB9Cn0sewogICJmbGlnaHROdW1iZXIiOjEsCiAgIndoZW4iOiIyMDE3LTA4LTAyVDE3OjQ4OjQ3LjkzOFoiLAogICJjb21tYW5kIjp7CiAgICAibmFtZSI6IkNsaWNrIiwKICAgICJpbiI6Iltvcmcub3BlbnFhLnNlbGVuaXVtLnJlbW90ZS5SZW1vdGVXZWJFbGVtZW50QGYwOTc1OTM3IC0+IHVua25vd24gbG9jYXRvcl0iLAogICAgImJ5cyI6W3sKICAgICAgImlkIjoiaWQiCiAgICB9XSwKICAgICJhcmdzIjp7CiAgICAgIAogICAgfQogIH0sCiAgImNvbnRleHQiOnsKICAgICJmYWlsdXJlcyI6WyJmYWlsZWQgZm9yIGEgYml0Il0sCiAgICAic3VjY2VzcyI6InRydWUiCiAgfQp9LHsKICAiZmxpZ2h0TnVtYmVyIjoxLAogICJ3aGVuIjoiMjAxNy0wOC0wMlQxNzo0ODo0Ny45MzlaIiwKICAiY29tbWFuZCI6ewogICAgIm5hbWUiOiJBc3NlcnRFbGVtZW50TGlzdFRleHRFcXVhbHMiLAogICAgImluIjoiW29yZy5vcGVucWEuc2VsZW5pdW0ucmVtb3RlLlJlbW90ZVdlYkVsZW1lbnRAZjA5NzU5MzcgLT4gdW5rbm93biBsb2NhdG9yXSIsCiAgICAiYnlzIjpbewogICAgICAiaWQiOiJpZCIKICAgIH1dLAogICAgImFyZ3MiOnsKICAgICAgCiAgICB9LAogICAgImV4cGVjdGVkTWFueSI6WyJleHBlY3RlZCJdCiAgfSwKICAiY29udGV4dCI6ewogICAgImZhaWx1cmVzIjpbImZhaWx1cmVzIl0sCiAgICAic3VjY2VzcyI6ImZhbHNlIgogIH0KfV0="))
            in
                case result of
                    Ok x -> update ParseData { model | raw = x }
                    Err e -> ({ model | raw = data, error = Just e }, Cmd.none )

--                update ParseData { model | raw = data }

        ParseData ->
            let
                result = (Json.Decode.decodeString decodeInvestigationList model.raw)
                model_ = case result of
                    Ok x -> { model | investigations = x}
                    Err e -> { model | error = Just e }
            in
            ( model_, Cmd.none )


port allFlightsData : (String -> msg) -> Sub msg

subscriptions : Model -> Sub Msg
subscriptions model =
  allFlightsData LoadData


main : Program Never Model Msg
main =
    program
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }
