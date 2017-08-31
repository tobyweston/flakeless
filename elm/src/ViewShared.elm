port module ViewShared exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)


gapRight : (String, String)
gapRight = ("margin-right", "6px")

grey : (String, String)
grey = ("color", "grey")

smaller : (String, String)
smaller = ("font-size", "smaller")

nowt : Html msg
nowt =
    text ""
