package com.expenseai

import com.expenseai.ai.SmsParser
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SmsParserTest {

    private lateinit var parser: SmsParser

    @Before fun setup() { parser = SmsParser() }

    @Test fun `parses HDFC debit alert`() {
        val sms = "HDFC Bank: Rs.1,500.00 debited from A/c XX4321 on 05-04-26. Avl Bal:Rs.12,345.67. If not done by you call 18002586161"
        val result = parser.parse(sms)
        assertNotNull(result)
        assertEquals(1500.0, result!!.amount, 0.01)
    }

    @Test fun `parses ICICI credit card spend`() {
        val sms = "ICICI Bank: INR 2500.00 spent on Credit Card XX5678 at AMAZON on 06-Apr-26."
        val result = parser.parse(sms)
        assertNotNull(result)
        assertEquals(2500.0, result!!.amount, 0.01)
        assertTrue(result.vendor.contains("AMAZON", ignoreCase = true))
    }

    @Test fun `parses Axis UPI debit`() {
        val sms = "Rs.350 debited from A/c XX9876 to VPA swiggy@axisbank on 06-04-2026 (UPI Ref No 123456789012). -Axis Bank"
        val result = parser.parse(sms)
        assertNotNull(result)
        assertEquals(350.0, result!!.amount, 0.01)
    }

    @Test fun `parses SBI ATM withdrawal`() {
        val sms = "SBI: Your A/c XXXXXX1234 is debited for Rs 5000.00 at ATM on 06/04/2026. Available Balance is Rs 20,000.00."
        val result = parser.parse(sms)
        assertNotNull(result)
        assertEquals(5000.0, result!!.amount, 0.01)
    }

    @Test fun `parses Kotak net banking transfer`() {
        val sms = "Kotak Bank: Rs.800.00 transferred to ZOMATO via Net Banking on 05-04-2026. Ref no. 987654321"
        val result = parser.parse(sms)
        assertNotNull(result)
        assertEquals(800.0, result!!.amount, 0.01)
    }

    @Test fun `parses amount with comma separators`() {
        val sms = "HDFC Bank: Rs.12,500.00 debited from A/c XX1111 on 06-04-26."
        val result = parser.parse(sms)
        assertNotNull(result)
        assertEquals(12500.0, result!!.amount, 0.01)
    }

    @Test fun `parses whole number amount`() {
        val sms = "SBI: Your A/c debited for Rs 200 at merchant on 06/04/2026."
        val result = parser.parse(sms)
        assertNotNull(result)
        assertEquals(200.0, result!!.amount, 0.01)
    }

    @Test fun `parses rupee symbol amount`() {
        val sms = "UPI: ₹450.00 debited from XX5432 to PhonePe@ybl on 06-04-2026."
        val result = parser.parse(sms)
        assertNotNull(result)
        assertEquals(450.0, result!!.amount, 0.01)
    }

    @Test fun `extracts date in dd-MM-yy format`() {
        val sms = "HDFC: Rs.100 debited on 06-04-26."
        val result = parser.parse(sms)
        assertNotNull(result)
        assertTrue(result!!.date.isNotBlank())
    }

    @Test fun `extracts date in dd-MM-yyyy format`() {
        val sms = "SBI: Rs.100 debited on 06-04-2026."
        val result = parser.parse(sms)
        assertNotNull(result)
        assertTrue(result!!.date.isNotBlank())
    }

    @Test fun `returns null for OTP message`() {
        val sms = "Your OTP for login is 123456. Do not share with anyone. -HDFC Bank"
        val result = parser.parse(sms)
        assertNull(result)
    }

    @Test fun `returns null for promotional message`() {
        val sms = "Exclusive offer! Get 20% off on your next purchase. Click here."
        val result = parser.parse(sms)
        assertNull(result)
    }

    @Test fun `dedupKey is stable for same message`() {
        val sms = "HDFC: Rs.100 debited on 06-04-2026. Ref 111222333"
        val k1 = SmsParser.dedupKey(sms)
        val k2 = SmsParser.dedupKey(sms)
        assertEquals(k1, k2)
    }

    @Test fun `dedupKey differs for different messages`() {
        val sms1 = "HDFC: Rs.100 debited on 06-04-2026. Ref 111222333"
        val sms2 = "HDFC: Rs.200 debited on 06-04-2026. Ref 444555666"
        assertNotEquals(SmsParser.dedupKey(sms1), SmsParser.dedupKey(sms2))
    }
}
