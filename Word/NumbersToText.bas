Attribute VB_Name = "NumbersToText"
Function NumberToText(ByVal value As Long) As String
    Dim units As Variant ' jednosci
    Dim teens As Variant ' nastki
    Dim tens As Variant ' dziesiatki
    Dim hundreds As Variant ' setki
    Dim thousands As Variant ' tysiace
    
    units = Array("", "jeden", "dwa", "trzy", "cztery", "piêæ", "szeœæ", "siedem", "osiem", "dziewiêæ")
    teens = Array("dziesiêæ", "jedenaœcie", "dwanaœcie", "trzynaœcie", "czternaœcie", "piêtnaœcie", "szesnaœcie", "siedemnaœcie", "osiemnaœcie", "dziewiêtnaœcie")
    tens = Array("", "dziesiêæ", "dwadzieœcia", "trzydzieœci", "czterdzieœci", "piêædziesi¹t", "szeœædziesi¹t", "siedemdziesi¹t", "osiemdziesi¹t", "dziewiêædziesi¹t")
    hundreds = Array("", "sto", "dwieœcie", "trzysta", "czterysta", "piêæset", "szeœæset", "siedemset", "osiemset", "dziewiêæset")
    thousands = Array("", "tysi¹c", "tysi¹ce", "tysiêcy")
    
    Dim wholePart As Long ' czesc calkowita
    wholePart = Int(value)
 
    Dim result As String ' rezultat tekstowy
    result = ""

    If wholePart = 0 Then
        result = "zero"
    Else
        result = ConvertHundreds(wholePart, units, tens, teens, hundreds, thousands)
    End If

    NumberToText = result
    
End Function

Function ConvertHundreds(ByVal number As Long, ByVal units As Variant, ByVal tens As Variant, ByVal teens As Variant, ByVal hundreds As Variant, ByVal thousands As Variant) As String
    Dim result As String
    result = ""

    If number >= 1000 Then
        result = result & ConvertHundreds(Int(number / 1000), units, tens, teens, hundreds, thousands) & " " & GetThousandsName(number / 1000, thousands) & " "
        number = number Mod 1000
    End If

    If number >= 100 Then
        result = result & hundreds(Int(number / 100)) & " "
        number = number Mod 100
    End If

    If number >= 10 And number < 20 Then
        result = result & teens(number - 10) & " "
    ElseIf number >= 20 Then
        result = result & tens(Int(number / 10)) & " "
        number = number Mod 10
    End If

    If number > 0 And number < 10 Then
        result = result & units(number) & " "
    End If

    ConvertHundreds = Trim(result)
End Function

Function GetThousandsName(ByVal number As Long, ByVal thousands As Variant) As String
    Select Case number
        Case 1
            GetThousandsName = thousands(1)
        Case 2 To 4
            GetThousandsName = thousands(2)
        Case Else
            GetThousandsName = thousands(3)
    End Select
End Function

