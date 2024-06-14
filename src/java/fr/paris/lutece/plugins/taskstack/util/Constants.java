/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.taskstack.util;

public class Constants
{

    public static final String PLUGIN_PATH = "taskstack";

    public static final String PARAM_CLIENT_CODE = "client_code";
    public static final String PARAM_APPLICATION_CODE = "application_code";
    public static final String PARAM_AUTHOR_NAME = "author_name";
    public static final String PARAM_AUTHOR_TYPE = "author_type";

    public static final String TASK_PATH = "/task";
    public static final String TASK_CODE_PARAM = "task_code";
    public static final String TASK_STATUS_PATH = "/status";
    public static final String TASK_STATUS_PATH_WITH_PARAM = TASK_STATUS_PATH + "/{" + TASK_CODE_PARAM + "}";
    public static final String SEARCH_TASK_PATH = "/search";

    public static final String KEY_STATUS = "status";
    public static final String KEY_RESPONSE = "response";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_MESSAGE_KEY = "message_key";
    public static final String KEY_HTTP_CODE = "http_code";

    public static final String PROPERTY_REST_INFO_SUCCESSFUL_OPERATION = "taskstack.rest.info.successful.operation";
}
