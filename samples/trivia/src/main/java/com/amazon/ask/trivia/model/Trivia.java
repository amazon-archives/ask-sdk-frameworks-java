/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.trivia.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Trivia {
    private Locale locale;
    private List<Question> questions;

    public Trivia() {
        this(Locale.ENGLISH, new ArrayList<>());
    }

    public Trivia(Locale locale) {
        this(locale , new ArrayList<>());
    }

    public Trivia(Locale locale, List<Question> questions) {
        this.locale = locale;
        this.questions = questions;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
