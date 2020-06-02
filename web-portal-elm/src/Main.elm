module Main exposing (main)

import Browser
import Html exposing (..)
import Html.Attributes exposing (href)
import Html.Events exposing (onClick)
import Http exposing (..)
import Json.Decode as Decode exposing (Decoder, int, list, string)
import Json.Decode.Pipeline exposing (required)
import RemoteData exposing (RemoteData, WebData)


type alias User =
    { id : Int
    , firstName : String
    , lastName : String
    , email : String
    }


type alias Model =
    { users : WebData (List User)
    }


view : Model -> Html Msg
view model =
    div []
        [ button [ onClick FetchUsers ]
            [ text "Refresh users" ]
        , viewUsersOrError model
        ]


viewUsersOrError : Model -> Html Msg
viewUsersOrError model =
    case model.users of
        RemoteData.NotAsked ->
            text ""

        RemoteData.Loading ->
            h3 [] [ text "Loading..." ]

        RemoteData.Success users ->
            viewUsers users

        RemoteData.Failure httpError ->
            viewError (buildErrorMessage httpError)


viewError : String -> Html Msg
viewError errorMessage =
    let
        errorHeading =
            "Couldn't fetch data at this time."
    in
    div []
        [ h3 [] [ text errorHeading ]
        , text ("Error: " ++ errorMessage)
        ]


viewUsers : List User -> Html Msg
viewUsers users =
    div []
        [ h3 [] [ text "Users" ]
        , table []
            ([ viewTableHeader ] ++ List.map viewUser users)
        ]


viewTableHeader : Html Msg
viewTableHeader =
    tr []
        [ th []
            [ text "ID" ]
        , th []
            [ text "Title" ]
        , th []
            [ text "Author" ]
        ]


viewUser : User -> Html Msg
viewUser user =
    tr []
        [ td []
            [ text (String.fromInt user.id) ]
        , td []
            [ text user.email ]
        , td []
            [ a [ href user.firstName ] [ text user.firstName ] ]
        ]


type Msg
    = FetchUsers
    | UsersReceived (WebData (List User))


userDecoder : Decoder User
userDecoder =
    Decode.succeed User
        |> required "id" int
        |> required "email" string
        |> required "firstName" string
        |> required "lastName" string


fetchUsers : Cmd Msg
fetchUsers =
    let
        headers =
            [ Http.header "Authorization" "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTExMzYwMTcsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJlM2ZhMDFkMC02MmNhLTQ4ZjAtODk5Yi1hYzgyOTAzOGNhNDQiLCJjbGllbnRfaWQiOiJkd2gtY2xpZW50Iiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXX0.cARpv_oJwVC7wwwCbhVCZRtuLLdhbGoKju_dBBKd9Bc" ]
    in
    Http.request
        { method = "GET"
        , headers = headers
        , url = "/api/security/users?page=0&size=5"
        , body = Http.emptyBody
        , expect =
            list userDecoder
                |> Http.expectJson (RemoteData.fromResult >> UsersReceived)
        , timeout = Nothing
        , tracker = Nothing
        }


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        FetchUsers ->
            ( { model | users = RemoteData.Loading }, fetchUsers )

        UsersReceived response ->
            ( { model | users = response }, Cmd.none )


buildErrorMessage : Http.Error -> String
buildErrorMessage httpError =
    case httpError of
        Http.BadUrl message ->
            message

        Http.Timeout ->
            "Server is taking too long to respond. Please try again later."

        Http.NetworkError ->
            "Unable to reach server."

        Http.BadStatus statusCode ->
            "Request failed with status code: " ++ String.fromInt statusCode

        Http.BadBody message ->
            message


init : () -> ( Model, Cmd Msg )
init _ =
    ( { users = RemoteData.Loading }, fetchUsers )


main : Program () Model Msg
main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }
