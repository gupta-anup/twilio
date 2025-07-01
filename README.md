# Twilio SMS Service

A production-ready Spring Boot application for sending SMS messages using Twilio's API.

## Features

- 🚀 **SMS Sending**: Send SMS messages via Twilio API
- 🔒 **Input Validation**: Comprehensive validation for phone numbers and message content
- 📊 **Error Handling**: Global exception handling with detailed error responses
- 🏥 **Health Checks**: Built-in health monitoring endpoints
- 📝 **Logging**: Structured logging with phone number masking for privacy
- 🧪 **Testing**: Comprehensive unit and integration tests
- 📚 **API Documentation**: OpenAPI/Swagger integration

## Prerequisites

- Java 21+
- Maven 3.6+
- Twilio Account (Account SID, Auth Token, and Phone Number)

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd twilio-sms-service
```

### 2. Configure Twilio Credentials

Set your Twilio credentials as environment variables:

```bash
export TWILIO_ACCOUNT_SID=your_account_sid
export TWILIO_AUTH_TOKEN=your_auth_token
export TWILIO_PHONE_NUMBER=your_twilio_phone_number
```

Or update `src/main/resources/application.yaml` directly (not recommended for production).

### 3. Build and Run

```bash
# Build the application
mvn clean compile

# Run tests
mvn test

# Start the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Send SMS

**POST** `/api/v1/sms/send`

Send an SMS message to a recipient.

**Request Body:**
```json
{
  "to": "+1234567890",
  "message": "Hello from our application!"
}
```

**Success Response (200):**
```json
{
  "sid": "SM1234567890abcdef",
  "to": "+1234567890",
  "from": "+0987654321",
  "body": "Hello from our application!",
  "status": "sent",
  "errorCode": null,
  "errorMessage": null,
  "sentAt": "2025-01-15T10:30:00",
  "success": true,
  "message": "SMS sent successfully"
}
```

**Error Response (400):**
```json
{
  "sid": null,
  "to": "+1234567890",
  "from": null,
  "body": null,
  "status": "failed",
  "errorCode": "SEND_ERROR",
  "errorMessage": "Error description",
  "sentAt": "2025-01-15T10:30:00",
  "success": false,
  "message": "Failed to send SMS"
}
```

### Health Check

**GET** `/api/v1/sms/health`

Check if the SMS service is running.

**Response:**
```
SMS Service is running
```

### Application Health

**GET** `/actuator/health`

Spring Boot Actuator health endpoint.

## Validation Rules

- **Phone Number (`to`)**:
  - Required
  - Must be in E.164 format (e.g., `+1234567890`)
  - Pattern: `^\\+[1-9]\\d{1,14}$`

- **Message (`message`)**:
  - Required
  - Maximum length: 1600 characters

## Configuration

### Application Properties

| Property | Description | Default | Environment Variable |
|----------|-------------|---------|---------------------|
| `twilio.account-sid` | Twilio Account SID | - | `TWILIO_ACCOUNT_SID` |
| `twilio.auth-token` | Twilio Auth Token | - | `TWILIO_AUTH_TOKEN` |
| `twilio.phone-number` | Twilio Phone Number | - | `TWILIO_PHONE_NUMBER` |
| `server.port` | Application port | `8080` | `SERVER_PORT` |

### Logging

The application uses structured logging with the following features:

- Phone number masking for privacy (shows only first 3 and last 2 digits)
- File and console logging
- Log files saved to `logs/twilio-sms-service.log`

## Security Best Practices

1. **Environment Variables**: Store sensitive credentials as environment variables
2. **Phone Number Masking**: Phone numbers are masked in logs for privacy
3. **Input Validation**: All inputs are validated before processing
4. **Error Handling**: Detailed error responses without exposing sensitive information

## Architecture

```
├── config/
│   ├── TwilioConfig.java          # Twilio client configuration
│   └── TwilioProperties.java      # Configuration properties
├── controller/
│   └── SmsController.java         # REST API endpoints
├── dto/
│   ├── SmsRequest.java           # Request DTO with validation
│   └── SmsResponse.java          # Response DTO
├── exception/
│   └── GlobalExceptionHandler.java # Global error handling
└── service/
    ├── SmsService.java           # Service interface
    └── impl/
        └── TwilioSmsService.java # Twilio implementation
```

## Error Codes

| Code | Description |
|------|-------------|
| `VALIDATION_ERROR` | Invalid request parameters |
| `SEND_ERROR` | Failed to send SMS through Twilio |
| `INTERNAL_ERROR` | Unexpected application error |

## Development

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=SmsControllerTest

# Generate test coverage report
mvn jacoco:report
```

### API Documentation

Access the interactive API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Production Deployment

### Environment Variables

Ensure the following environment variables are set:

```bash
TWILIO_ACCOUNT_SID=your_actual_account_sid
TWILIO_AUTH_TOKEN=your_actual_auth_token
TWILIO_PHONE_NUMBER=your_actual_phone_number
SPRING_PROFILES_ACTIVE=prod
```

### Docker Deployment

```dockerfile
FROM openjdk:21-jre-slim
COPY target/twilio-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Health Monitoring

Monitor application health using:
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/api/v1/sms/health` - SMS service specific health

## Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## License

This project is licensed under the MIT License.
