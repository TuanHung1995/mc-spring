package com.mc.domain.work.model.enums;

/**
 * BoardColumnType — Domain Enum (Work Context)
 *
 * <p>Defines the data type of a Board Column.
 * Each column type renders differently in the UI and stores data differently.</p>
 */
public enum BoardColumnType {
    /** Free-form text input */
    TEXT,
    /** Assigns a user from the workspace to a row */
    PERSON,
    /** Colored status label (e.g., Working on it, Done, Stuck) */
    STATUS,
    /** Calendar date picker */
    DATE,
    /** Numeric value */
    NUMBER,
    /** Checkbox (boolean) */
    CHECKBOX,
    /** External hyperlink */
    LINK
}
