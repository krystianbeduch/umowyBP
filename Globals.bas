Attribute VB_Name = "Globals"
Public doc As Document ' Globalny obiekt dokumentu
Public conn As Object ' Globalne polaczenie z baza danych
Public config As AccessDatabaseConfig ' Globalna konfiguracja bazy danych Access
Public Const DATABASE_PATH As String = "E:\Dokumenty z C\US\BP Wesley\umowy-bp.accdb"
Public wycieczka As KlasaWycieczka ' Globalny obiekt konkretnej wycieczki
Public zamawiajacy As KlasaZamawiajacy ' Globalny obiekt konkretnego zamawiajacego
Public bookmarkRange As Range ' Globalna zmienna do lokalizacji zakladek

Public Sub InitializeConnection()
    ' Inicjalizacja polaczenia z baza danych
    Set config = New AccessDatabaseConfig
    config.Path = DATABASE_PATH
    
    ' Uzyskaj ciag polaczenia do bazy danych Access
    Dim strConnection As String
    strConnection = config.ConnectionString
    
    ' Tworzenie nowego polaczenia
    Set conn = New ADODB.Connection
    conn.Open strConnection
End Sub


