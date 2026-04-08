package com.expenseai

import com.expenseai.ai.EmailParser
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EmailParserTest {

    private lateinit var parser: EmailParser

    @Before fun setup() { parser = EmailParser() }

    @Test fun `parses Amazon order amount`() {
        val email = """
            Your Amazon order #123-4567890-1234567
            Order Total: Rs. 1,299.00
            Estimated delivery: 08 Apr 2026
        """.trimIndent()
        val result = parser.parse(email, subject = "Your Amazon.in order")
        assertNotNull(result)
        assertEquals(1299.0, result!!.amount, 0.01)
    }

    @Test fun `parses Swiggy receipt`() {
        val email = """
            Thank you for your order from Burger King!
            Items total: Rs 450
            Delivery fee: Rs 30
            Grand Total: Rs 480
            Order placed: 06 Apr 2026
        """.trimIndent()
        val result = parser.parse(email, subject = "Your Swiggy order")
        assertNotNull(result)
        assertEquals(480.0, result!!.amount, 0.01)
        assertTrue(result.vendor.contains("Swiggy", ignoreCase = true) ||
                   result.vendor.contains("Burger", ignoreCase = true))
    }

    @Test fun `parses IRCTC booking`() {
        val email = """
            Booking Confirmation
            Train: 12345 RAJDHANI EXP
            Amount Paid: INR 1850.00
            Date of Journey: 10-04-2026
            PNR: 1234567890
        """.trimIndent()
        val result = parser.parse(email, subject = "IRCTC Booking Confirmation")
        assertNotNull(result)
        assertEquals(1850.0, result!!.amount, 0.01)
    }

    @Test fun `parses hotel booking`() {
        val email = """
            Booking ID: HTL-987654
            Hotel: Taj Palace
            Check-in: 2026-04-15
            Total Amount: Rs.8500.00
        """.trimIndent()
        val result = parser.parse(email, subject = "Hotel Booking Confirmed")
        assertNotNull(result)
        assertEquals(8500.0, result!!.amount, 0.01)
    }

    @Test fun `extracts vendor from subject when body ambiguous`() {
        val email = "Total: Rs 500\nDate: 06 Apr 2026"
        val result = parser.parse(email, subject = "Your Zomato order receipt")
        assertNotNull(result)
        assertTrue(result!!.vendor.contains("Zomato", ignoreCase = true))
    }

    @Test fun `defaults date to today when not found`() {
        val email = "Amount: Rs 200"
        val result = parser.parse(email, subject = "Receipt")
        assertNotNull(result)
        assertTrue(result!!.date.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
    }

    @Test fun `returns null for non-transactional email`() {
        val email = "Hi! Just checking in. Hope you're doing well."
        val result = parser.parse(email, subject = "Hello from a friend")
        assertNull(result)
    }

    @Test fun `handles HTML-stripped email`() {
        val email = "Order Total Rs 750 placed on 06 Apr 2026"
        val result = parser.parse(email, subject = "Flipkart order")
        assertNotNull(result)
        assertEquals(750.0, result!!.amount, 0.01)
    }

    @Test fun `dedup key is stable`() {
        val email = "Order #ABC-123 Total Rs 500"
        val k1 = EmailParser.dedupKey(email, "Subject")
        val k2 = EmailParser.dedupKey(email, "Subject")
        assertEquals(k1, k2)
    }
}
