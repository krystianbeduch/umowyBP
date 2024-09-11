Attribute VB_Name = "Dates"
Option Explicit

Sub UpdateTransportDate(ByVal ContentControl As ContentControl, ByVal transportTitle As String)
    Dim cc As ContentControl
    
    If isMultiDay Then
        ' Znajdz formanty "termin_wyjazdu" i "termin_przyjazdu" i skopiuj daty
        For Each cc In ActiveDocument.ContentControls
            If cc.Title = transportTitle Then
                cc.Range.text = ContentControl.Range.text
                Exit For
            End If
        Next cc
    Else
        ' Znajdz formant 'termin wjazdu' i skopiuj do 2 formantow transportu
        If ContentControl.Title = "termin_wyjazdu" Then
            For Each cc In ActiveDocument.ContentControls
                If cc.Tag = "termin_transport" Then
                    cc.Range.text = ContentControl.Range.text
                End If
            Next cc
        End If
    End If
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

