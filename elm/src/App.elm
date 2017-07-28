module Main exposing (..)

import Html exposing (..)
import Http exposing (..)
import RemoteData exposing (..)
import DataPointCodec exposing (..)


type alias Model =
    String


type Msg
    = DataPointsResponse (WebData (List DataPoint))


init : ( Model, Cmd Msg )
init =
    ( "Hello", Cmd.none )


view : Model -> Html Msg
view model =
    div []
        [ text model ]


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        DataPointsResponse response ->
            ( model, Cmd.none )


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none


main : Program Never Model Msg
main =
    program
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }

loadDataPoints : Cmd Msg
loadDataPoints  =
    Http.get "" decodeDataPointList
    |> RemoteData.sendRequest
    |> Cmd.map DataPointsResponse