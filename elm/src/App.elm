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
        li [ style [ ("color", color) ] ] [
            span [ ] [
                span [style [ ("margin-right", "7px")]] [text (DateFormat.format config "%H:%M:%S.%L" dataPoint.when)]
                , if MaybeExtra.isJust dataPoint.description then span [style [ ("margin-right", "7px")] ] [text (dataPoint.description |> Maybe.withDefault "") ] else nowt
                , renderCommand dataPoint.command
                , renderContext dataPoint.context
            ]
        ]

renderCommand : Maybe Command -> Html msg
renderCommand maybeCommand =
    case maybeCommand of
        Nothing -> nowt
        Just command -> span [] [
            span [style [ ("margin-right", "7px")]] [text command.name]
            , renderBys command.bys
            , renderIn command.in_
            , renderExpected command.expected command.expectedMany
            --TODO: whack me when done ....
--            , div [style [ ("margin-right", "7px")]] [ text (toString command) ]
            ]

renderExpected : Maybe String -> Maybe (List String) -> Html msg
renderExpected expected expectedMany =
    case (expected, expectedMany) of
        (Just e, _) -> span [style [ ("margin-right", "7px")]] [ span [style [ ("margin-right", "7px")]] [text "expected" ], text ("\"" ++ e ++ "\"") ]
        (_, Just me) -> span [style [ ("margin-right", "7px")]] [ span [style [ ("margin-right", "7px")]] [text "expected" ], text (toString me) ]
        (_, _) -> nowt

renderBys : List (List (String, String)) -> Html msg
renderBys bys =
    if List.isEmpty bys then nowt else span [style [ ("margin-right", "7px")]]
      ((List.map (\b -> renderBy b) bys) |> List.intersperse (text " -> "))


renderBy : List (String, String) -> Html msg
renderBy by =
    let
      (key, value) = List.head by |> Maybe.withDefault ("???", "???")
    in
      text (key ++ ": " ++ value)

renderIn : Maybe String -> Html msg
renderIn maybeIn =
    case maybeIn of
        Nothing -> nowt
        Just in_ -> span [style [ ("margin-right", "7px")]] [ span [style [ ("margin-right", "7px")]] [text "in" ], text in_ ]

renderContext : Maybe Context -> Html msg
renderContext maybeContext =
    case maybeContext of
        Nothing -> nowt
        Just context -> if List.isEmpty context.failures then nowt
                        else ul [] (List.map (\f -> li [] [text f]) context.failures)

nowt : Html msg
nowt =
    text ""

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        LoadData data ->
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
