Attribute VB_Name = "Dates"
Sub UpdateTransportDate(ByVal ContentControl As ContentControl, ByVal transportTag As String)
    ' Znajdz formant "termin_od_transport" i skopiuj date
    Dim cc As ContentControl
    For Each cc In ThisDocument.ContentControls
        If cc.Tag = transportTag Then
            cc.Range.text = ContentControl.Range.text
            Exit For
        End If
    Next cc
End Sub

Sub UpdateTodayDate()
    ' Pobranie dzisiejszej daty
    Dim todayDate As Date
    todayDate = Date

    ' Pobranie formantu datowego "data_dzis"
    Dim cc As ContentControl
    On Error Resume Next
    Set cc = ActiveDocument.SelectContentControlsByTitle("data_dzis")(1)
    On Error GoTo 0
    
    ' Aktualizacja daty w formancie, jesli istnieje
    If Not cc Is Nothing Then
        cc.Range.text = Format(todayDate, "dd.mm.yyyy")
    Else
        MsgBox "Nie znaleziono formantu 'data_dzis'.", vbExclamation, "Error"
    End If
End Sub

