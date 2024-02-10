package com.walletwise.authservice.v1.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    private String receiver;
    private String subject;
    private String body;
}
