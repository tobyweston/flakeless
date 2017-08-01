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
--TODO: connect screenshot with it's datapoint
--TODO: for first cut maybe just have screenshot.png

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
        div [ style [ ("min-height", "20px") ] ] [
            span [ ] [
                span [style [ gapRight, smaller, grey ]] [text (DateFormat.format config "%H:%M:%S.%L" dataPoint.when)]
                , span [style [ ("color", color), ("font-weight", "bold"), gapRight ]] [ text "*"]
                , if MaybeExtra.isJust dataPoint.description then span [style [ gapRight, smaller ] ] [text (dataPoint.description |> Maybe.withDefault "") ] else nowt
                , renderCommand dataPoint.command
                , renderContext dataPoint.context
            ]
        ]

gapRight : (String, String)
gapRight = ("margin-right", "5px")

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
            , renderIn command.in_
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

renderBys : List (List (String, String)) -> Html msg
renderBys bys =
    if List.isEmpty bys then nowt else span [class "lozenge" ,style [ smaller, gapRight ]]
      ((List.map (\b -> renderBy b) bys) |> List.intersperse (span [style [ smaller ]] [text " > "]))


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
                        span [style [smaller]] [span [style [grey, smaller]] [ text "actual: "], span [] ((List.map (\f -> span [] [text ("\"" ++ f ++ "\"")]) context.failures) |> List.intersperse (span [style [ smaller ]] [text ", "]))]
                        else nowt

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
