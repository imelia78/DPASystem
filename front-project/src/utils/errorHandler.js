export const parseApiError = (err, defaultMsg = 'An error occurred connecting to the server. Please try again later.') => {
  let errorMsg = defaultMsg;
  
  if (err.response?.data) {
    const data = err.response.data;
    
    // Handle field validation errors gracefully
    if (data.fieldErrors && Object.keys(data.fieldErrors).length > 0) {
      const errors = Object.entries(data.fieldErrors)
        .map(([field, msg]) => `${field}: ${msg}`)
        .join(' | ');
      errorMsg = `Please check your inputs: ${errors}`;
    } 
    // Handle standard message or error strings
    else if (data.message) {
      errorMsg = data.message;
    } else if (typeof data === 'string') {
      errorMsg = data;
    } else if (data.error) {
      errorMsg = data.error;
    }
  }
  
  // UX Dictionary: Map ugly backend exceptions to friendly messages
  if (errorMsg.includes('Resource already exists or violates a constraint')) {
    errorMsg = 'Action failed: This resource (e.g. Email, Phone, Certificate) is already registered or violates a rule.';
  } else if (errorMsg.includes('Invalid user credentials')) {
    errorMsg = 'Invalid email or password. Please try again.';
  } else if (errorMsg.includes('User exists with same username')) {
    errorMsg = 'An account with this email is already registered.';
  } else if (errorMsg.includes('Client or Doctor mismatch')) {
    errorMsg = 'You are not authorized to perform this action for this user.';
  } else if (errorMsg.includes('Review already exists')) {
    errorMsg = 'You have already submitted a review for this appointment.';
  }

  return errorMsg;
};
