Attribute VB_Name = "PickupLocation"
Option Explicit

Sub AddPickupAndDropOffLocations()
    ' Inicjalizacja polaczenia z baza danych
    If Not InitializeConnection_PostgreSQL() Then
        MsgBox "Nie udalo sie nawiazac polaczenia z baza danych PostgreSQL.", vbCritical, "Error"
        Exit Sub
    End If
    
    ' Zapytanie SQL do pobrania danych
    Dim sqlProvider As New SQLQueryProvider

    Dim rs As Object ' Zmienna dla zestawu wynikow
    Set rs = conn.Execute(sqlProvider.SelectClientWithPickupLocation(client.clientNumber))
    
    ' Sprawdz wynik i utworz tekst do wstawienia do formantow
    If Not rs.EOF Then
        If rs.Fields("pickup_location").value = "*Adres*" Then
            ' Jesli "miejsce_odbioru" jest zgodne ze wzorcem "*Adres" - jest takie samo jak adres
            With client.PickupLocation
                .PickupLocation = ""
                .city = client.city
                .street = client.street
                .number = client.number
                .postCode = client.postCode
            End With
        Else
            ' Miejsce odbioru jest ustalone w innym miejscu
            With client.PickupLocation
                .PickupLocation = IIf(IsNull(rs.Fields("pickup_location").value), "", rs.Fields("pickup_location").value)
                .city = IIf(IsNull(rs.Fields("pickup_city").value), "", rs.Fields("pickup_city").value)
                .street = IIf(IsNull(rs.Fields("pickup_street").value), "", rs.Fields("pickup_street").value)
                .number = IIf(IsNull(rs.Fields("pickup_number").value), "", rs.Fields("pickup_number").value)
                .postCode = IIf(IsNull(rs.Fields("pickup_post_code").value), "", rs.Fields("pickup_post_code").value)
            End With
        End If
    Else
        MsgBox "Nie znaleziono wynikow dla zapytania.", vbExclamation, "Error"
    End If
    
    ' Zamkniecie polaczenia
    rs.Close
    conn.Close
    Set rs = Nothing
    Set conn = Nothing
End Sub
