port module ViewShared exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)


gapRight : (String, String)
gapRight = ("margin-right", "6px")

grey : (String, String)
grey = ("color", "grey")

smaller : (String, String)
smaller = ("font-size", "smaller")

larger : (String, String)
larger = ("font-size", "larger")

bold : (String, String)
bold = ("font-weight", "bold")

nowt : Html msg
nowt =
    text ""
