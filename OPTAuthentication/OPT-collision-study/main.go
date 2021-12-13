package main

import (
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"strconv"
)

var OTP [32]byte
var truncated string
var counter int

var repeat int
var OtpSet map[string]string

func main() {
	repeat = 0
	counter = 1
	OtpSet = make(map[string]string)
	collision()
}

//Use the same algorithm as the OTP generator
func collision() {

	decoded, _ := hex.DecodeString("800770FF00FF08012")
	OTP = sha256.Sum256(decoded)
	truncated = hex.EncodeToString(OTP[:3])

	for i := 1; i <= 1000000; i++ {
		OTP = sha256.Sum256(OTP[:])
		truncated = hex.EncodeToString(OTP[:3])
		if len(OtpSet[truncated]) != 0 {
			repeat += 1
		} else {
			OtpSet[truncated] = "Contained"
		}

		if counter%100000 == 0 {
			fmt.Println(strconv.Itoa(counter) + ": " + strconv.Itoa(repeat))
		}
		counter += 1
	}

}
