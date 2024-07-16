Attribute VB_Name = "Globals"
Public doc As Document ' Globalny obiekt dokumentu
Public conn As Object ' Globalne po��czenie z baz� danych
Public config As AccessDatabaseConfig ' Globalna konfiguracja bazy danych Access
Public Const DATABASE_PATH As String = "E:\Dokumenty z C\US\BP Wesley\umowy-bp.accdb"
Public wycieczka As KlasaWycieczka ' Globalny obiekt konkretnej wycieczki
Public bookmarkRange As Range ' Globalna zmienna do lokalizacji zak�adek

Public Sub InitializeConnection()
    ' Inicjalizacja po��czenia z baz� danych
    Set config = New AccessDatabaseConfig
    config.Path = DATABASE_PATH
    
    ' Uzyskaj ci�g po��czenia do bazy danych Access
    Dim strConnection As String
    strConnection = config.ConnectionString
    
    ' Tworzenie nowego po��czenia
    Set conn = New ADODB.Connection
    conn.Open strConnection
End Sub


