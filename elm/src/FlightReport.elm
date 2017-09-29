port module FlightReport exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Http exposing (..)
import RemoteData exposing (..)
import FlightReportCodec exposing (..)
import ViewShared exposing (..)
import Json.Decode exposing (decodeString)
import Maybe.Extra as MaybeExtra
import Date.Extra.Format as DateFormat
import Date.Extra.Config.Config_en_gb exposing (config)
import Dict
import Base64
import Json.Encode exposing (string)

--TODO: kill bullets!
--TODO: make logs be expand/collapsible
--TODO: collapse logs by defaiult, unless error

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
    , flightDataRecord : FlightDataRecord
    , error : Maybe String
    }


type Msg
    = LoadData String
    | ParseData


init : ( Model, Cmd Msg )
init =
    ( Model "" (FlightDataRecord "" "" []) Nothing, Cmd.none )


view : Model -> Html Msg
view model =
    let
        isError = MaybeExtra.isJust model.error
    in
        div []
            [ if isError then div [] [text (model.error |> Maybe.withDefault "") ] else nowt
            , div [style [larger, bold, grey, ("margin-bottom", "6px")]] [text model.flightDataRecord.suite, text " - ", text model.flightDataRecord.test]
            , div [] (List.map (\dp -> renderDataPoint dp ) model.flightDataRecord.dataPoints)
            , if isError then div [] [
                hr [] []
                , text ("raw:" ++ toString model.raw)
                ]
                else nowt
            ]


--TODO: render DP number instead of bullet
--TODO: be able to toggle duration to date vs actual time, default to first
renderDataPoint : DataPoint -> Html msg
renderDataPoint dataPoint =
    let
        colorClass = case dataPoint.context of
                    Nothing -> "message"
                    Just context -> case context.success of
                        Nothing -> "dunno"
                        Just success -> if success then "pass" else "fail"
    in
        div [ style [ ("min-height", "20px"), ("padding-bottom", "5px") ] ] [
            span [ class colorClass ] [rightArrow, text " "]
            , span [ ] [
                span [style [ gapRight, smaller, grey ]] [text (DateFormat.format config "%H:%M:%S.%L" dataPoint.when)]
--                , span [style [ ("color", colorClass), ("font-weight", "bold"), gapRight ]] [ text "*"]
                , if MaybeExtra.isJust dataPoint.description then span [style [ gapRight, smaller ] ] [text (dataPoint.description |> Maybe.withDefault "") ] else nowt
                , renderCommand dataPoint.command
                , renderContext dataPoint.context
                , renderLog dataPoint.log
            ]
        ]


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
      span [ style [ grey] ] [ text "{" ]
      , span [] ((List.map (\b -> renderBy b) bys) |> List.intersperse (span [style [ smaller ]] [span [style [smaller,("font-style", "bold" )]] [ text " -> "]]))
      , span [ style [ grey] ] [ text "}" ]
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
                        div [style [("margin-left", "20px"),("margin-top", "6px")]] [span [style [grey, smaller]] [ text "actual: "], span [ style [ ("color", "#cc0000") ]] [text (List.reverse context.failures |> List.head |> Maybe.map (\f -> "\"" ++ f ++ "\"") |> Maybe.withDefault "???" )] ]
                        else nowt


renderLog : Maybe (List String) -> Html msg
renderLog maybeLog =
    case maybeLog of
        Nothing -> nowt
        Just log -> div [style [ smaller, grey, ("margin-left", "25px") ]] [ pre [ style [ smaller, ("white-space", "pre-wrap") ]] [text ("\n" ++ (String.join "\n" log)) ] ]


rightArrow : Html msg
rightArrow =
    span [ property "innerHTML" (string "&#9658;") ] []


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
                result = (Json.Decode.decodeString decodeFlightDataRecord model.raw)
                model_ = case result of
                    Ok x -> { model | flightDataRecord = x}
                    Err e -> { model | error = Just e }
            in
            ( model_, Cmd.none )


port flightData : (String -> msg) -> Sub msg


subscriptions : Model -> Sub Msg
subscriptions model =
  flightData LoadData


main : Program Never Model Msg
main =
    program
        { init = init
        , view = view
        , update = update
        , subscriptions = subscriptions
        }
