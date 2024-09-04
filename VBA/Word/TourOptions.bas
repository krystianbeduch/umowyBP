Attribute VB_Name = "TourOptions"
Sub SetMinNumberOfPeople()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    Dim minNumberOfPeople As Integer
    Dim numberOfPeople As Integer
    numberOfPeople = GetDocProperty(DOC_PROP_NUMBER_OF_PEOPLE)
    
    ' Sprawdzenie typu wycieczki i ustawienie odpowiednich przedzialow
    If typeOfTour = True Then ' Wycieczka szkolna
        Select Case numberOfPeople
            Case 54 To 59
                minNumberOfPeople = 54
            Case 46 To 53
                minNumberOfPeople = 46
            Case 40 To 45
                minNumberOfPeople = 40
            Case 33 To 39
                minNumberOfPeople = 33
            Case 29 To 32
                minNumberOfPeople = 29
            Case 24 To 28
                minNumberOfPeople = 24
            Case 19 To 23
                minNumberOfPeople = 19
            Case Else
                MsgBox "Liczba osob nie miesci sie w zadnym przedziale", vbExclamation, "Error"
                Exit Sub
        End Select
    Else
        ' Wycieczka dla doroslych
        Select Case numberOfPeople
            Case 54 To 59
                minNumberOfPeople = 54
            Case 46 To 53
                minNumberOfPeople = 46
            Case 41 To 45
                minNumberOfPeople = 41
            Case 36 To 40
                minNumberOfPeople = 36
            Case 28 To 35
                minNumberOfPeople = 28
            Case 24 To 27
                minNumberOfPeople = 24
            Case 20 To 23
                minNumberOfPeople = 20
            Case Else
                MsgBox "Liczba osob nie miesci sie w zadnym przedziale", vbExclamation, "Error"
                Exit Sub
        End Select
    End If
    
    ' Ustaw minimalna liczbe osob do formantu
    Dim cc As ContentControl
    On Error Resume Next
    Set cc = ActiveDocument.SelectContentControlsByTitle("min_liczba_osob")(1)
    On Error GoTo 0
    
    If Not cc Is Nothing Then
        cc.Range.text = minNumberOfPeople
    Else
        MsgBox "Nie znaleziono formantu 'min_liczba_osob'.", vbExclamation, "Error"
    End If
    
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
    Exit Sub
End Sub

Sub SetCareeFree()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    ' Pobierz aktualn¹ liczbê osób z DocProperty "ile_osob"
    Dim numberOfPeople As Integer
    
    numberOfPeople = GetDocProperty(DOC_PROP_NUMBER_OF_PEOPLE)
    
    ' SprawdŸ typ wycieczki
    If typeOfTour = False Then
        ' Wycieczka dla doros³ych - wyczysc formant
        Call InsertTextToContentControl("opieka_gratis", " ")
    ElseIf typeOfTour = True Then
        ' Wycieczka szkolna - ustaw odpowiedni tekst w zaleznosci od liczby osob
        Dim careFreeText As String
        Select Case numberOfPeople
            Case 54 To 59
                careFreeText = "(4 x opieka gratis)"
            Case 46 To 53
                careFreeText = "(4 x opieka gratis)"
            Case 40 To 45
                careFreeText = "(3 x opieka gratis)"
            Case 33 To 39
                careFreeText = "(3 x opieka gratis)"
            Case 29 To 32
                careFreeText = "(3 x opieka gratis)"
            Case 24 To 28
                careFreeText = "(2 x opieka gratis)"
            Case 19 To 23
                careFreeText = "(2 x opieka gratis)"
            Case Else
                careFreeText = "" ' Dla wartoœci poza zakresem, nie ustawiaj niczego
        End Select
        
        ' Ustaw tekst w formancie "opieka_gratis"
        Call InsertTextToContentControl("opieka_gratis", careFreeText)
    End If
    
    Exit Sub
    
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Sub SetInsurance()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    Dim insuranceText As String
    If locationOfTour Then  ' True - Polska
        insuranceText = "NNW (100 000,- z³) i KL (10 000,- z³)"
    Else                    ' False - Europa
        insuranceText = "NNW (100 000,- z³) i KL (10 000,- z³) Europa"
    End If
    
    ' Ustaw tekst ubezpieczenia
    Call InsertTextToContentControl("ubezpieczenia", insuranceText)
    
    Exit Sub
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

Sub SetSchedule()
    On Error GoTo ErrorHandler ' Ustawienie obslugi bledow
    
    Dim doc As Document
    Set doc = ActiveDocument
    Dim rng As Range
    
    ' Sprawdzenie czy zakladka 'terminarz' istnieje w dokumencie
    If doc.Bookmarks.Exists("terminarz") Then
        ' Ustawienie zakresu w miejscu zakladki
        Set rng = doc.Bookmarks("terminarz").Range
        
        ' Usuniecie tekstu od miejsca zakladki do konca linii
        rng.End = rng.Paragraphs(1).Range.End - 1
        rng.text = ""
    Else
        MsgBox "Zakladka 'terminarz' nie istnieje w dokumencie.", vbExclamation, "Error"
    End If
    
    ' Sprawdzenie czy zakres zostal prawidlowo ustalony
    If rng Is Nothing Then Exit Sub
        
    ' Utworzenie nowego formantu daty 'termin_wyjazdu'
    Dim ccFromDate As ContentControl
    Set ccFromDate = doc.ContentControls.Add(wdContentControlDate, rng)
    ccFromDate.Title = "termin_wyjazdu"
    Dim todayDate As Date
    todayDate = Date
    ccFromDate.Range.text = Format(todayDate, "dd.mm.yy")
    
    rng.SetRange ccFromDate.Range.End, ccFromDate.Range.End
    rng.MoveEnd wdCharacter, 1
    rng.Collapse wdCollapseEnd
    
    UpdateTransportDate ccFromDate, "termin_od_transport"
        
    If isMultiDay Then ' wycieczka wielodniowa (2 terminarze)
        rng.text = " - "
        rng.Collapse wdCollapseEnd
        
        ' Utworzenie nowego formantu daty 'termin_przyjazdu'
        Dim ccToDate As ContentControl
        rng.Select
        Set ccToDate = doc.ContentControls.Add(wdContentControlDate, rng)
        ccToDate.Title = "termin_przyjazdu"
        ccToDate.Range.text = todayDate + 1
        
        rng.SetRange ccFromDate.Range.End, ccFromDate.Range.End
        rng.MoveEnd wdCharacter, 1
        rng.Collapse wdCollapseEnd
        
        UpdateTransportDate ccToDate, "termin_do_transport"
    End If
    Exit Sub

ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
End Sub

