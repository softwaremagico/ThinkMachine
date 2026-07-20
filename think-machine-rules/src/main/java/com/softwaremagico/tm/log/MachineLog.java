package com.softwaremagico.tm.log;

/*
 * #%L
 * KendoTournamentGenerator
 * %%
 * Copyright (C) 2008 - 2012 Softwaremagico
 * %%
 * This software is designed by Jorge Hortelano Otero. Jorge Hortelano Otero
 * <softwaremagico@gmail.com> Valencia (Spain).
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MachineLog {

    private static final Logger logger = LoggerFactory.getLogger(MachineLog.class);

    private MachineLog() {

    }

    /**
     * Events that have business meaning (i.e. creating category, deleting form, ...). To follow user actions.
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    private static void info(String messageTemplate, Object... arguments) {
        logger.info(messageTemplate, arguments);
    }

    /**
     * Events that have business meaning (i.e. creating category, deleting form, ...). To follow user actions.
     *
     * @param className       the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void info(String className, String messageTemplate, Object... arguments) {
        info(className + ": " + messageTemplate, arguments);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not allowed for this user, ...
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    private static void warning(String messageTemplate, Object... arguments) {
        logger.warn(messageTemplate, arguments);
    }

    /**
     * Shows not critical errors. I.e. Email address not found, permissions not allowed for this user, ...
     *
     * @param className       the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void warning(String className, String messageTemplate, Object... arguments) {
        warning(className + ": " + messageTemplate, arguments);
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application access to a method, opening database
     * connection, etc.
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    private static void debug(String messageTemplate, Object... arguments) {
        if (isDebugEnabled()) {
            logger.debug(messageTemplate, arguments);
        }
    }

    /**
     * For following the trace of the execution. I.e. Knowing if the application access to a method, opening database
     * connection, etc.
     *
     * @param className       the class to log.
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void debug(String className, String messageTemplate, Object... arguments) {
        debug(className + ": " + messageTemplate, arguments);
    }

    /**
     * To log any not expected error that can cause application malfuncionality. I.e. couldn't open database connection,
     * etc..
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    private static void severe(String messageTemplate, Object... arguments) {
        logger.error(messageTemplate, arguments);
    }

    /**
     * To log any not expected error that can cause application malfuncionality.
     *
     * @param messageTemplate string with static text as template.
     * @param arguments       parameters to fill up the template
     */
    public static void severe(String className, String messageTemplate, Object... arguments) {
        severe(className + ": " + messageTemplate, arguments);
    }

    /**
     * To log java exceptions and log also the stack trace.
     *
     * @param className the class to log.
     * @param throwable the exception to log.
     */
    public static void errorMessage(String className, Throwable throwable) {
        logger.error("Exception on class {}:\n", className, throwable);
    }

    public static boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }
}
