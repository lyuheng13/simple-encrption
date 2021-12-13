package main

import (
	"crypto/sha256"
	"encoding/hex"
	"fmt"

	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/data/binding"
	"fyne.io/fyne/v2/widget"
)

var counter int
var OTP [32]byte

var truncated string
var entry *string

var resultString string
var result *string

var input binding.ExternalString
var output binding.ExternalString

func main() {
	counter = 1

	resultString := "Please enter your OTP"
	result = &resultString
	output = binding.BindString(result)
	input = binding.BindString(entry)
	app := app.New()

	w := app.NewWindow("OTP Checker")
	c := container.NewVBox(
		widget.NewLabelWithData(output),
		widget.NewEntryWithData(input),
		widget.NewButton("Click", checkOTP),
	)
	w.SetContent(c)

	w.ShowAndRun()
}

//Called when a button is clicked, check if the OTP is valid
func checkOTP() {
	input.Reload()
	inputHex, err := input.Get()
	if err != nil {
		resultString := "Wrong OTP!"
		result = &resultString
		output.Reload()
	}
	//fmt.Println(inputHex)

	//Generate OTP
	if counter == 1 {
		decoded, _ := hex.DecodeString("800770FF00FF08012")
		OTP = sha256.Sum256(decoded)
		truncated = hex.EncodeToString(OTP[:3])
	}

	if inputHex == truncated {
		resultString := "Checked!"
		fmt.Println("Check")
		result = &resultString
		output.Reload()

		counter += 1
		OTP = sha256.Sum256(OTP[:])
		truncated = hex.EncodeToString(OTP[:3])
	} else {
		resultString := "Wrong OTP!"
		fmt.Println("Wrong")
		result = &resultString
		output.Reload()
	}
}
