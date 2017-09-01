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
import Table


--TODO: link through to reports (if exist)
--TODO: split out suite and test
--TODO: use http://package.elm-lang.org/packages/evancz/elm-sortable-table/1.0.1
--TODO: add search
--TODO: find some way to track flaky tests etc, needs a server ... atomic Int/Map on a server
-- .... useful for tracking flaky reasons, reporters etc, any kind of tracking really
--TODO: tick items to count (maybe)
--TODO: inflightAnnounce when some rawWebDriver stuff done ....

type alias Model =
    { raw : String
    , investigations : List Investigation
    , error : Maybe String
    , tableState : Table.State
    }


type Msg
    = LoadData String
    | ParseData
    | SetTableState Table.State


init : ( Model, Cmd Msg )
init =
    ( Model "" [] Nothing (Table.initialSort "Test"), Cmd.none )


view : Model -> Html Msg
view model =
    let
        isError = MaybeExtra.isJust model.error
    in
        div []
            [ if isError then div [] [text (model.error |> Maybe.withDefault "") ] else nowt
            , Table.view config model.tableState model.investigations
--            , ul [] (List.map (\i -> renderInvestigation i ) model.investigations)
            , if isError then div [] [
                hr [] []
                , text ("raw:" ++ toString model.raw)
                ]
                else nowt
            ]

config : Table.Config Investigation Msg
config =
  Table.config
    { toId = .name
    , toMsg = SetTableState
    , columns =
        [ Table.intColumn "Test" .flightNumber
        , durationColumn
        , durationColumn2
        , Table.stringColumn "Name" .name
        ]
    }


durationColumn : Table.Column Investigation Msg
durationColumn =
  Table.customColumn
    { name = "Duration (millis)"
    , viewData = toString << maybeDurationToInt << .durationMillis
    , sorter = Table.decreasingOrIncreasingBy (maybeDurationToInt << .durationMillis)
    }

durationColumn2 : Table.Column Investigation Msg
durationColumn2 =
  Table.customColumn
    { name = "Duration2 (millis)"
    , viewData = toString << maybeDurationToInt << .durationMillis2
    , sorter = Table.decreasingOrIncreasingBy (maybeDurationToInt << .durationMillis2)
    }


maybeDurationToInt : Maybe Int -> Int
maybeDurationToInt duration =
  duration |> Maybe.withDefault -1


--renderInvestigation : Investigation -> Html msg
--renderInvestigation investigation =
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
--    div [] [text (toString investigation)]


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        LoadData data ->
            let
                result = Base64.decode data

--                d = Debug.log "b64" (toString (Base64.decode "blah"))
            in
                case result of
                    Ok x -> update ParseData { model | raw = x }
                    Err e -> ({ model | raw = data, error = Just e }, Cmd.none )

        ParseData ->
            let
                result = (Json.Decode.decodeString decodeInvestigationList model.raw)
                model_ = case result of
                    Ok x -> { model | investigations = x}
                    Err e -> { model | error = Just e }
            in
            ( model_, Cmd.none )

        SetTableState newState ->
            ( { model | tableState = newState }
            , Cmd.none
            )


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
