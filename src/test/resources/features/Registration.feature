Feature: Player registration
  As a visitor to GameTwist
  I want to open the registration form
  So that I can begin creating an account
   
  Background: Open GameTwist home page
    Given i am on the GameTwist home page
    When i accept all cookies
    And i dismiss browser notification alert

  Scenario: Enter registration details

    Given I generate registration test data
    When i choose to register
    Then the registration form is displayed
    When I enter registration details from JSON
    #And i click captcha
    And i click terms checkbox
    And i click begin adventure button
    Then Verify email from API
    And Verify email confirmation text
    And i close email confirmation popup
    Then i login to application
    And i close wheel of fortune popup
    Then i click on collect button
    And i click on not now remind me later radio button
    And i click on i opt out benifits radio button
    Then i click on confirm button
    Then i click on collect button
    And i click on profile icon
    And i click on personal data link
    Then i click on change security question button
    And i select a security question from the drop down
    And i enter an answer for the security question
    And i enter Enter Password Security Model
    Then i click on Save Changes button
    And verify that The security question and answer have been changed
    Then i close Security Question popUp
    And i click on news letter consent
    Then i click on collect button
    Then i verify Accept text for news letter consent
    And i click on profile icon
    Then i click on logout button