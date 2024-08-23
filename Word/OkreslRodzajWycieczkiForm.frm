VERSION 5.00
Begin {C62A69F0-16DC-11CE-9E98-00AA00574A4F} OkreslRodzajWycieczkiForm 
   Caption         =   "Okreœl rodzaj wycieczki"
   ClientHeight    =   3795
   ClientLeft      =   105
   ClientTop       =   450
   ClientWidth     =   6225
   OleObjectBlob   =   "OkreslRodzajWycieczkiForm.frx":0000
   StartUpPosition =   1  'CenterOwner
End
Attribute VB_Name = "OkreslRodzajWycieczkiForm"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False

Private Sub btnSetType_Click()
    On Error GoTo ErrorHandler
    If rbtnSchool.value = True Then ' Szkolna
        typeOfTour = True
    ElseIf rbtnAdults.value = True Then ' Dorosli
        typeOfTour = False
    End If
    
    If rbtnPoland.value = True Then ' Polska
        locationOfTour = True
    ElseIf rbtnEurope.value = True Then ' Europa
        locationOfTour = False
    End If
    
    If rbtnOneDay.value = True Then ' 1-dniowa
        isMultiDay = False
    ElseIf rbtnMultiDay.value = True Then ' wielodniowa
        isMultiDay = True
    End If
    
    ' Zaktualizuj minimalna liczbe osob
    SetMinNumberOfPeople
    
    ' Ustaw opiekunow gratis
    SetCareeFree
    
    ' Ustaw ubzpieczenia
    SetInsurance
    
    ' Ustaw terminarz
    SetSchedule
    
    ' Zamknij formularz
    Unload Me
    Exit Sub
ErrorHandler:
    MsgBox "Wystapil blad: " & Err.Description, vbCritical, "Error"
    Exit Sub
    
End Sub

Private Sub UserForm_Initialize()
    If typeOfTour Then ' True - szkolna
        rbtnSchool.value = True
    Else                ' False - dorosli
        rbtnAdults.value = True
    End If
    
    If locationOfTour Then ' True - Polska
        rbtnPoland.value = True
    Else                    ' False - Europa
        rbtnEurope.value = True
    End If
    
    If isMultiDay Then ' True - wielodniowa
        rbtnMultiDay.value = True
    Else
        rbtnOneDay.value = True
    End If
    
    btnSetType.SetFocus
End Sub
