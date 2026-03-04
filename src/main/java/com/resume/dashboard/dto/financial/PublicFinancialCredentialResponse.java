package com.resume.dashboard.dto.financial;

import java.time.LocalDate;

public class PublicFinancialCredentialResponse {

    private String credentialType;
    private String certificationName;
    private String issuingAuthority;
    private LocalDate validTill;
    private String region;
    private String verificationUrl;
    private boolean verified;
	public String getCredentialType() {
		return credentialType;
	}
	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}
	public String getCertificationName() {
		return certificationName;
	}
	public void setCertificationName(String certificationName) {
		this.certificationName = certificationName;
	}
	public String getIssuingAuthority() {
		return issuingAuthority;
	}
	public void setIssuingAuthority(String issuingAuthority) {
		this.issuingAuthority = issuingAuthority;
	}
	public LocalDate getValidTill() {
		return validTill;
	}
	public void setValidTill(LocalDate validTill) {
		this.validTill = validTill;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getVerificationUrl() {
		return verificationUrl;
	}
	public void setVerificationUrl(String verificationUrl) {
		this.verificationUrl = verificationUrl;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

    // getters & setters
    
}