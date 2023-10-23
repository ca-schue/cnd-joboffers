package thi.cnd.userservice.core.port.secondary.event.company;

import thi.cnd.userservice.core.port.secondary.event.Event;

public sealed interface CompanyEvent extends Event permits CompanyDeletedEvent, CompanyNameChangedEvent, CompanyRegisteredEvent {
}