package com.gogidix.transaction.onboarding.statemachine;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker.OnboardingStage;
import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker.OnboardingStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class OnboardingStateMachineConfig extends EnumStateMachineConfigurerAdapter<OnboardingStage, OnboardingStatus> {

    @Override
    public void configure(StateMachineStateConfigurer<OnboardingStage, OnboardingStatus> states) throws Exception {
        states
            .withStates()
            .initial(OnboardingStage.REGISTRATION)
            .states(EnumSet.allOf(OnboardingStage.class))
            .end(OnboardingStage.COMPLETED)
            .end(OnboardingStage.REGISTRATION);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OnboardingStage, OnboardingStatus> transitions) throws Exception {
        transitions
            // Standard flow
            .withExternal()
                .source(OnboardingStage.REGISTRATION).target(OnboardingStage.KYC_VERIFICATION)
                .event(OnboardingStatus.IN_PROGRESS)
                .and()
            .withExternal()
                .source(OnboardingStage.KYC_VERIFICATION).target(OnboardingStage.BUSINESS_VERIFICATION)
                .event(OnboardingStatus.IN_PROGRESS)
                .and()
            .withExternal()
                .source(OnboardingStage.BUSINESS_VERIFICATION).target(OnboardingStage.DOCUMENT_UPLOAD)
                .event(OnboardingStatus.IN_PROGRESS)
                .and()
            .withExternal()
                .source(OnboardingStage.DOCUMENT_UPLOAD).target(OnboardingStage.DOCUMENT_VERIFICATION)
                .event(OnboardingStatus.IN_PROGRESS)
                .and()
            .withExternal()
                .source(OnboardingStage.DOCUMENT_VERIFICATION).target(OnboardingStage.BANK_ACCOUNT_SETUP)
                .event(OnboardingStatus.IN_PROGRESS)
                .and()
            .withExternal()
                .source(OnboardingStage.BANK_ACCOUNT_SETUP).target(OnboardingStage.COMPLIANCE_CHECK)
                .event(OnboardingStatus.IN_PROGRESS)
                .and()
            .withExternal()
                .source(OnboardingStage.COMPLIANCE_CHECK).target(OnboardingStage.RISK_ASSESSMENT)
                .event(OnboardingStatus.IN_PROGRESS)
                .and()
            .withExternal()
                .source(OnboardingStage.RISK_ASSESSMENT).target(OnboardingStage.APPROVAL)
                .event(OnboardingStatus.IN_PROGRESS)
                .and()
            .withExternal()
                .source(OnboardingStage.APPROVAL).target(OnboardingStage.ACTIVATION)
                .event(OnboardingStatus.APPROVED)
                .and()
            .withExternal()
                .source(OnboardingStage.ACTIVATION).target(OnboardingStage.COMPLETED)
                .event(OnboardingStatus.COMPLETED)
                .and()
            // Rejection flow
            .withExternal()
                .source(OnboardingStage.APPROVAL).target(OnboardingStage.REGISTRATION)
                .event(OnboardingStatus.REJECTED)
                .and()
            // Verification pending flows
            .withExternal()
                .source(OnboardingStage.KYC_VERIFICATION).target(OnboardingStage.KYC_VERIFICATION)
                .event(OnboardingStatus.PENDING_VERIFICATION)
                .and()
            .withExternal()
                .source(OnboardingStage.DOCUMENT_VERIFICATION).target(OnboardingStage.DOCUMENT_VERIFICATION)
                .event(OnboardingStatus.PENDING_VERIFICATION)
                .and()
            // Failure flows
            .withExternal()
                .source(OnboardingStage.KYC_VERIFICATION).target(OnboardingStage.REGISTRATION)
                .event(OnboardingStatus.FAILED)
                .and()
            .withExternal()
                .source(OnboardingStage.BUSINESS_VERIFICATION).target(OnboardingStage.REGISTRATION)
                .event(OnboardingStatus.FAILED)
                .and()
            .withExternal()
                .source(OnboardingStage.DOCUMENT_VERIFICATION).target(OnboardingStage.DOCUMENT_UPLOAD)
                .event(OnboardingStatus.FAILED)
                .and()
            // On hold flow
            .withExternal()
                .source(OnboardingStage.COMPLIANCE_CHECK).target(OnboardingStage.COMPLIANCE_CHECK)
                .event(OnboardingStatus.ON_HOLD)
                .and()
            .withExternal()
                .source(OnboardingStage.RISK_ASSESSMENT).target(OnboardingStage.RISK_ASSESSMENT)
                .event(OnboardingStatus.ON_HOLD);
    }

    public static class StateMachineListener extends StateMachineListenerAdapter<OnboardingStage, OnboardingStatus> {
        @Override
        public void stateChanged(State<OnboardingStage, OnboardingStatus> from, State<OnboardingStage, OnboardingStatus> to) {
            System.out.printf("State changed from %s to %s%n", from, to);
        }
    }
}
