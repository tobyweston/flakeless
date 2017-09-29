port module ViewShared exposing (..)

import Html exposing (..)
import Html.Attributes exposing (..)
import Json.Encode exposing (string)


gapRight : (String, String)
gapRight = ("margin-right", "6px")

grey : (String, String)
grey = ("color", "grey")

darkGrey : Html msg -> Html msg
darkGrey symbol =
  Html.span [ style [("color", "#555")] ] [ Html.text (" "), symbol, Html.text (" ") ]

lightGrey : Html msg -> Html msg
lightGrey symbol =
  Html.span [ style [("color", "#ccc")] ] [ Html.text (" "), symbol, Html.text (" ") ]

smaller : (String, String)
smaller = ("font-size", "smaller")

larger : (String, String)
larger = ("font-size", "larger")

bold : (String, String)
bold = ("font-weight", "bold")

nowt : Html msg
nowt =
    text ""

--TIP: https://stackoverflow.com/questions/2701192/what-characters-can-be-used-for-up-down-triangle-arrow-without-stem-for-displa
upArrow : Html msg
upArrow =
    span [ property "innerHTML" (string "&#9650;") ] []

downArrow : Html msg
downArrow =
    span [ property "innerHTML" (string "&#9660;") ] []

rightArrow : Html msg
rightArrow =
    span [ property "innerHTML" (string "&#9658;") ] []
