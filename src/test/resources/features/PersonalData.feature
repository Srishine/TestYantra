 Feature: Player personal Data management
   
  Background: Open GameTwist home page
    Given i am on the GameTwist home page
    When i accept all cookies
    And i dismiss browser notification alert

    Scenario: User change personal data

    And i close wheel of fortune popup
    And i click on profile icon
    And i click on personal data link
    And i select a security question from the drop down
    And i enter an answer for the security question
    And i enter password "<password>"
    Then i click on Save Changes button
    And verify that The security question and answer have been changed
    And i close wheel of fortune popup
    And i click on not now remind me later radio button
    And i click on i opt out benifits radio button
    Then i click on collect button
    #Then i click on Chanage Security question button
    And i click on news letter consent
    Then i click on collect button
    And i click on profile icon
    Then i click on logout button