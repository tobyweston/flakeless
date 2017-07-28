port module Main exposing (..)

import Html exposing (..)
import Http exposing (..)
import RemoteData exposing (..)
import DataPointCodec exposing (..)
import Json.Decode exposing (decodeString)

type alias Model =
    String


type Msg
    = LoadData String
--    = DataPointsResponse (WebData (List DataPoint))


init : ( Model, Cmd Msg )
init =
    ( "Thinking...", Cmd.none )


view : Model -> Html Msg
view model =
    div []
        [ text model ]


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        LoadData data ->
            (toString (Json.Decode.decodeString decodeDataPointList data) , Cmd.none)

--        DataPointsResponse response ->
--            ( (toString response), Cmd.none )


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