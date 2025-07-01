# SMS API Usage Examples

This document provides examples of how to use the Twilio SMS API endpoints.

## Base URL
```
http://localhost:8080
```

## 1. Health Check

### Check SMS Service Health
```bash
curl -X GET http://localhost:8080/api/v1/sms/health
```

**Response:**
```
SMS Service is running
```

### Check Application Health (Actuator)
```bash
curl -X GET http://localhost:8080/actuator/health
```

**Response:**
```json
{
  "status": "UP"
}
```

## 2. Send SMS

### Send an SMS Message
```bash
curl -X POST http://localhost:8080/api/v1/sms/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": "+1234567890",
    "message": "Hello from our application! This is a test message."
  }'
```

**Success Response (200 OK):**
```json
{
  "sid": "SM1234567890abcdef",
  "to": "+1234567890",
  "from": "+15393373967",
  "body": "Hello from our application! This is a test message.",
  "status": "sent",
  "errorCode": null,
  "errorMessage": null,
  "sentAt": "2025-07-01T15:30:45",
  "success": true,
  "message": "SMS sent successfully"
}
```

**Error Response (400 Bad Request):**
```json
{
  "sid": null,
  "to": "+1234567890",
  "from": null,
  "body": null,
  "status": "failed",
  "errorCode": "SEND_ERROR",
  "errorMessage": "Authentication Error - invalid username",
  "sentAt": "2025-07-01T15:30:45",
  "success": false,
  "message": "Failed to send SMS"
}
```

## 3. Validation Examples

### Invalid Phone Number Format
```bash
curl -X POST http://localhost:8080/api/v1/sms/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": "invalid-phone",
    "message": "Test message"
  }'
```

**Response (400 Bad Request):**
```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "Invalid request parameters",
  "details": {
    "to": "Phone number must be in E.164 format (e.g., +1234567890)"
  },
  "timestamp": "2025-07-01T15:30:45"
}
```

### Empty Message
```bash
curl -X POST http://localhost:8080/api/v1/sms/send \
  -H "Content-Type: application/json" \
  -d '{
    "to": "+1234567890",
    "message": ""
  }'
```

**Response (400 Bad Request):**
```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "Invalid request parameters",
  "details": {
    "message": "Message is required"
  },
  "timestamp": "2025-07-01T15:30:45"
}
```

## 4. Programming Examples

### JavaScript/Node.js
```javascript
const axios = require('axios');

async function sendSms(to, message) {
  try {
    const response = await axios.post('http://localhost:8080/api/v1/sms/send', {
      to: to,
      message: message
    });
    
    console.log('SMS sent successfully:', response.data);
    return response.data;
  } catch (error) {
    console.error('Failed to send SMS:', error.response?.data || error.message);
    throw error;
  }
}

// Usage
sendSms('+1234567890', 'Hello from JavaScript!');
```

### Python
```python
import requests
import json

def send_sms(to, message):
    url = 'http://localhost:8080/api/v1/sms/send'
    payload = {
        'to': to,
        'message': message
    }
    
    try:
        response = requests.post(url, json=payload)
        response.raise_for_status()
        
        result = response.json()
        print(f"SMS sent successfully: {result}")
        return result
    except requests.exceptions.RequestException as e:
        print(f"Failed to send SMS: {e}")
        if hasattr(e, 'response') and e.response is not None:
            print(f"Error details: {e.response.text}")
        raise

# Usage
send_sms('+1234567890', 'Hello from Python!')
```

### Java
```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SmsClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public void sendSms(String to, String message) throws Exception {
        var request = new SmsRequest(to, message);
        String json = objectMapper.writeValueAsString(request);
        
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/v1/sms/send"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
            
        HttpResponse<String> response = httpClient.send(httpRequest, 
            HttpResponse.BodyHandlers.ofString());
            
        if (response.statusCode() == 200) {
            System.out.println("SMS sent successfully: " + response.body());
        } else {
            System.err.println("Failed to send SMS: " + response.body());
        }
    }
    
    record SmsRequest(String to, String message) {}
}
```

## 5. Environment Variables

For production deployment, set these environment variables:

```bash
export TWILIO_ACCOUNT_SID=your_actual_account_sid
export TWILIO_AUTH_TOKEN=your_actual_auth_token
export TWILIO_PHONE_NUMBER=your_actual_phone_number
```

## 6. Error Codes

| Code | Description |
|------|-------------|
| `VALIDATION_ERROR` | Invalid request parameters (phone format, empty message, etc.) |
| `SEND_ERROR` | Failed to send SMS through Twilio API |
| `INTERNAL_ERROR` | Unexpected application error |

## 7. Rate Limiting and Best Practices

- Phone numbers are logged with masking for privacy (e.g., +12***90)
- Message length is limited to 1600 characters
- Always use E.164 format for phone numbers
- Handle error responses appropriately in your client code
- Monitor the application health endpoints for production systems
