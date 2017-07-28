port module Main exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Http exposing (..)
import RemoteData exposing (..)
import DataPointCodec exposing (..)
import Json.Decode exposing (decodeString)
import Maybe.Extra as MaybeExtra

type alias Model =
    { raw : String
    , dataPoints : List DataPoint
    , error : Maybe String}


type Msg
    = LoadData String
    | ParseData


init : ( Model, Cmd Msg )
init =
    ( Model "" [] Nothing, Cmd.none )


view : Model -> Html Msg
view model =
    div []
        [ if MaybeExtra.isJust model.error then div [] [text (model.error |> Maybe.withDefault "") ] else nowt
        , div [] (List.map (\dp -> renderDataPoint dp )model.dataPoints)
        , hr [] []
        , text ("raw:" ++ toString model.raw)
        ]

renderDataPoint : DataPoint -> Html msg
renderDataPoint dataPoint =
    li [] [
    span [] [
        span [style [ ("margin-right", "7px")]] [text dataPoint.when]
        , if MaybeExtra.isJust dataPoint.description then span [style [ ("margin-right", "7px")] ] [text (dataPoint.description |> Maybe.withDefault "") ] else nowt
        , if MaybeExtra.isJust dataPoint.command then span [style [ ("margin-right", "7px")] ] [text (dataPoint.command |> Maybe.map (toString) |> Maybe.withDefault "") ] else nowt
        , if MaybeExtra.isJust dataPoint.context then span [style [ ("margin-right", "7px")] ] [text (dataPoint.context |> Maybe.map (toString) |> Maybe.withDefault "") ] else nowt
    ]
    ]

nowt : Html msg
nowt =
    text ""

update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        LoadData data ->
            let
                model_ = { model | raw = data }
            in
            update ParseData model_

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
