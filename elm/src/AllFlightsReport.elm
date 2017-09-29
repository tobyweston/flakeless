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
import Table exposing (..)
import Json.Encode exposing (string)

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
    ( Model "" [] Nothing (Table.initialSort "Id"), Cmd.none )


view : Model -> Html Msg
view model =
    let
        isError = MaybeExtra.isJust model.error
    in
        div [ ]
            [ if isError then div [] [text (model.error |> Maybe.withDefault "") ] else nowt
            , Table.view testsTableConfig model.tableState model.investigations
--            , ul [] (List.map (\i -> renderInvestigation i ) model.investigations)
            , hr [] []
            , div [ style [ smaller, grey ] ] [ div [] [ text "[1] from test start to finish"]
                     , div [] [ text "[2] from first datapoint to finish"] ]
            , if isError then div [] [
                hr [] []
                , text ("raw:" ++ toString model.raw)
                ]
                else nowt
            ]

--TODO: fix the arrow rendering in jetbrains servers ... hmmm ... see simpleTheadHelp in customizations
--TODO: do another table, grouped by Suite .. to show the totals ...
testsTableConfig : Table.Config Investigation Msg
testsTableConfig =
  Table.customConfig
    { toId = .test
    , toMsg = SetTableState
    , columns =
        [ Table.intColumn "Id" .flightNumber
--        , result (until it's fixed)
        , grossDuration
        , netDuration
        , Table.stringColumn "Suite" .suite
        , Table.stringColumn "Test" .test
        , Table.intColumn "Data Points" .dataPointCount
        ]
    , customizations =
            { defaultCustomizations | rowAttrs = toRowAttrs, thead = simpleThead }
    }


toRowAttrs : Investigation -> List (Attribute Msg)
toRowAttrs i =
  [ style [ smaller ]
  ]

result : Table.Column Investigation Msg
result =
  Table.customColumn
    { name = "Result"
    , viewData = boolToString << .success
    , sorter = Table.decreasingOrIncreasingBy (boolToString << .success)
    }

grossDuration : Table.Column Investigation Msg
grossDuration =
  Table.customColumn
    { name = "Gross Duration [1]"
    , viewData = toString << maybeDurationToInt << .grossDurationMillis
    , sorter = Table.decreasingOrIncreasingBy (maybeDurationToInt << .grossDurationMillis)
    }

netDuration : Table.Column Investigation Msg
netDuration =
  Table.customColumn
    { name = "Net Duration [2]"
    , viewData = toString << maybeDurationToInt << .netDurationMillis
    , sorter = Table.decreasingOrIncreasingBy (maybeDurationToInt << .netDurationMillis)
    }


maybeDurationToInt : Maybe Int -> Int
maybeDurationToInt duration =
  duration |> Maybe.withDefault -1

boolToString : Bool -> String
boolToString value =
  if value then "Pass" else "Fail"


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

--TIP: monkey patch the table ....
simpleThead : List (String, Status, Attribute msg) -> HtmlDetails msg
simpleThead headers =
  HtmlDetails [] (List.map simpleTheadHelp headers)


simpleTheadHelp : ( String, Status, Attribute msg ) -> Html msg
simpleTheadHelp (name, status, onClick) =
  let
    content =
      case status of
        Unsortable ->
          [ Html.text name ]

        Sortable selected ->
          [ Html.text name
          , if selected then darkGrey (span [] [downArrow]) else lightGrey (span [] [upArrow])
          ]

        Reversible Nothing ->
          [ Html.text name
          , lightGrey (span [] [downArrow, upArrow])
          ]

        Reversible (Just isReversed) ->
          [ Html.text name
          , darkGrey (if isReversed then (span [] [upArrow]) else (span [] [downArrow]))
          ]
  in
    Html.th [ onClick ] content

--TIP: https://stackoverflow.com/questions/2701192/what-characters-can-be-used-for-up-down-triangle-arrow-without-stem-for-displa
upArrow : Html msg
upArrow =
    span [ property "innerHTML" (string "&#9650;") ] []

downArrow : Html msg
downArrow =
    span [ property "innerHTML" (string "&#9660;") ] []

darkGrey : Html msg -> Html msg
darkGrey symbol =
  Html.span [ style [("color", "#555")] ] [ Html.text (" "), symbol, Html.text (" ") ]


lightGrey : Html msg -> Html msg
lightGrey symbol =
  Html.span [ style [("color", "#ccc")] ] [ Html.text (" "), symbol, Html.text (" ") ]
