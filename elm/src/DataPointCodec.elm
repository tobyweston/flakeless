module DataPointCodec exposing (..)
--exposing (decodeDatapoint)


import Json.Encode
import Json.Decode
import Json.Decode.Pipeline

type alias Datapoint0CommandBy =
    { id : String
    }

type alias Datapoint0CommandArgs =
    { key : String
    }

type alias Datapoint0Command =
    { name : String
    , in_ : String
    , by : Datapoint0CommandBy
    , args : Datapoint0CommandArgs
    , expected : String
    , expectedMany : List String
    }

type alias Datapoint0Context =
    { failures : List String
    , success : String
    }

type alias Datapoint =
    { flightNumber : Int
    , when : String
    , command : Datapoint0Command
    , context : Datapoint0Context
    }
--
--decodeDatapoint : Json.Decode.Decoder Datapoint
--decodeDatapoint =
--    Json.Decode.Pipeline.decode Datapoint
--        |> Json.Decode.Pipeline.required "0" (decodeDatapoint0)
--
--decodeDatapoint0CommandBy : Json.Decode.Decoder Datapoint0CommandBy
--decodeDatapoint0CommandBy =
--    Json.Decode.Pipeline.decode Datapoint0CommandBy
--        |> Json.Decode.Pipeline.required "id" (Json.Decode.string)
--
--decodeDatapoint0CommandArgs : Json.Decode.Decoder Datapoint0CommandArgs
--decodeDatapoint0CommandArgs =
--    Json.Decode.Pipeline.decode Datapoint0CommandArgs
--        |> Json.Decode.Pipeline.required "key" (Json.Decode.string)
--
--decodeDatapoint0Command : Json.Decode.Decoder Datapoint0Command
--decodeDatapoint0Command =
--    Json.Decode.Pipeline.decode Datapoint0Command
--        |> Json.Decode.Pipeline.required "name" (Json.Decode.string)
--        |> Json.Decode.Pipeline.required "in" (Json.Decode.string)
--        |> Json.Decode.Pipeline.required "by" (decodeDatapoint0CommandBy)
--        |> Json.Decode.Pipeline.required "args" (decodeDatapoint0CommandArgs)
--        |> Json.Decode.Pipeline.required "expected" (Json.Decode.string)
--        |> Json.Decode.Pipeline.required "expectedMany" (Json.Decode.list Json.Decode.string)
--
--decodeDatapoint0Context : Json.Decode.Decoder Datapoint0Context
--decodeDatapoint0Context =
--    Json.Decode.Pipeline.decode Datapoint0Context
--        |> Json.Decode.Pipeline.required "failures" (Json.Decode.list Json.Decode.string)
--        |> Json.Decode.Pipeline.required "success" (Json.Decode.string)
--
--decodeDatapoint0 : Json.Decode.Decoder Datapoint
--decodeDatapoint0 =
--    Json.Decode.Pipeline.decode Datapoint
--        |> Json.Decode.Pipeline.required "flightNumber" (Json.Decode.int)
--        |> Json.Decode.Pipeline.required "when" (Json.Decode.string)
--        |> Json.Decode.Pipeline.required "command" (decodeDatapoint0Command)
--        |> Json.Decode.Pipeline.required "context" (decodeDatapoint0Context)
--
