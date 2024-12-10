package com.manager.boats.rental.boats_rental.util;

import org.springframework.stereotype.Component;

@Component
public class ExtractErrorMessage {

    public String extractErrorMessage(String originalMessage) {
        String startMarker = "ERROR: ";
        String endMarker = "\n  Where:";
    
        int startIndex = originalMessage.indexOf(startMarker);
        if (startIndex == -1) return "Database error"; // Return generic if marker not found.
        startIndex += startMarker.length();
    
    
        int endIndex = originalMessage.indexOf(endMarker, startIndex);
        if (endIndex == -1) endIndex = originalMessage.length(); // go to end if not found
    
        return originalMessage.substring(startIndex, endIndex);
    }
}
