module DataPointCodec exposing (DataPoint, decodeDataPointList)


import Json.Encode
import Json.Decode
import Json.Decode.Pipeline
import Dict as Dict

type alias DataPoint =
    { flightNumber : Int
    , when : String
    , command : DataPointCommand
    , context : DataPointContext
    }

type alias DataPointCommandArgs =
    { key : String
    }

type alias DataPointCommand =
    { name : String
    , in_ : String
    , bys : List (List (String, String))
    , args : DataPointCommandArgs
    , expected : String
    , expectedMany : List String
    }

type alias DataPointContext =
    { failures : List String
    , success : String
    }

decodeDataPointList : Json.Decode.Decoder (List DataPoint)
decodeDataPointList =
    Json.Decode.list <| decodeDataPoint

decodeDataPoint : Json.Decode.Decoder DataPoint
decodeDataPoint =
    Json.Decode.Pipeline.decode DataPoint
        |> Json.Decode.Pipeline.required "flightNumber" (Json.Decode.int)
        |> Json.Decode.Pipeline.required "when" (Json.Decode.string)
        |> Json.Decode.Pipeline.required "command" (decodeDataPointCommand)
        |> Json.Decode.Pipeline.required "context" (decodeDataPointContext)

decodeDataPointCommandArgs : Json.Decode.Decoder DataPointCommandArgs
decodeDataPointCommandArgs =
    Json.Decode.Pipeline.decode DataPointCommandArgs
        |> Json.Decode.Pipeline.required "key" (Json.Decode.string)

decodeDataPointCommand : Json.Decode.Decoder DataPointCommand
decodeDataPointCommand =
    Json.Decode.Pipeline.decode DataPointCommand
        |> Json.Decode.Pipeline.required "name" (Json.Decode.string)
        |> Json.Decode.Pipeline.required "in" (Json.Decode.string)
        |> Json.Decode.Pipeline.required "bys" (Json.Decode.list decodeBys)
        |> Json.Decode.Pipeline.required "args" (decodeDataPointCommandArgs)
        |> Json.Decode.Pipeline.required "expected" (Json.Decode.string)
        |> Json.Decode.Pipeline.required "expectedMany" (Json.Decode.list Json.Decode.string)


decodeBys : Json.Decode.Decoder (List (String, String))
decodeBys =
    Json.Decode.keyValuePairs Json.Decode.string

decodeDataPointContext : Json.Decode.Decoder DataPointContext
decodeDataPointContext =
    Json.Decode.Pipeline.decode DataPointContext
        |> Json.Decode.Pipeline.required "failures" (Json.Decode.list Json.Decode.string)
        |> Json.Decode.Pipeline.required "success" (Json.Decode.string)
