port module Main exposing (..)

import Html exposing (..)
import Http exposing (..)
import RemoteData exposing (..)
import DataPointCodec exposing (..)
import Json.Decode exposing (decodeString)

type alias Model =
    { raw : String
    , dataPoints : List DataPoint
    , error : Maybe String}



type Msg
    = LoadData String
    | ParseData
--    = DataPointsResponse (WebData (List DataPoint))


init : ( Model, Cmd Msg )
init =
    ( Model "" [] Nothing, Cmd.none )


view : Model -> Html Msg
view model =
    div []
        [ div [] [text (model.error |> Maybe.withDefault "")]
        , div [] [text (toString (model.dataPoints))]
        , hr [] []
        , text (toString model.raw)
        ]


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

--loadDataPoints : Cmd Msg
--loadDataPoints  =
--    Http.get "/flakeless.json" decodeDataPointList
--    |> RemoteData.sendRequest
--    |> Cmd.map DataPointsResponse