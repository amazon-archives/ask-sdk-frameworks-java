/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.trivia.service;

import com.amazon.ask.trivia.model.Question;
import com.amazon.ask.trivia.model.Trivia;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TriviaDataProvider {
    private static List<Trivia> data = generateData();

    public TriviaDataProvider() {}

    public Trivia getData(Locale locale) {
        return data.stream()
            .filter(x -> x.getLocale().equals(locale))
            .findFirst()
            .get();
    }

    private static List<Trivia> generateData() {
        Trivia engbTrivia = new Trivia();
        engbTrivia.setLocale(Locale.UK);
        engbTrivia.setQuestions(Arrays.asList(
            new Question("Reindeer have very thick coats, how many hairs per square inch do they have?", Arrays.asList(
                "13,000","1,200","5,000","700","1,000","120,000"
            )),
            new Question("The 1964 classic Rudolph The Red Nosed Reindeer was filmed in? ", Arrays.asList(
                "Japan","United States","Finland","Germany","Canada","Norway","France"
            )),
            new Question("Santas reindeer are cared for by one of the Christmas elves, what is his name?", Arrays.asList(
                "Wunorse Openslae","Alabaster Snowball","Bushy Evergreen","Pepper Minstix"
            )),
            new Question("If all of Santas reindeer had antlers while pulling his Christmas sleigh, they would all be? ", Arrays.asList(
                "Girls","Boys","Girls and boys","No way to tell"
            )),
            new Question("What do Reindeer eat?", Arrays.asList(
                "Lichen","Grasses","Leaves","Berries"
            )),
            new Question("What of the following is not true?", Arrays.asList(
                "Caribou live on all continents","Both reindeer and Caribou are the same species","Caribou are bigger than reindeer","Reindeer live in Scandinavia and Russia"
            )),
            new Question("In what year did Rudolph make his television debut?", Arrays.asList(
                "1964","1979","2000","1956"
            )),
            new Question("Who was the voice of Rudolph in the 1964 classic?", Arrays.asList(
                "Billie Mae Richards","Burl Ives","Paul Soles","Lady Gaga"
            )),
            new Question("In 1939 what retailer used the story of Rudolph the Red Nose Reindeer?", Arrays.asList(
                "Montgomery Ward","Sears","Macys","Kmart"
            )),
            new Question("Santa&#39;s reindeer named Donner was originally named what?", Arrays.asList(
                "Dunder","Donny","Dweedle","Dreamy"
            )),
            new Question("Who invented the story of Rudolph?", Arrays.asList(
                "Robert May","Johnny Marks","Santa","J K  Rowling"
            )),
            new Question("In what location will you not find reindeer?", Arrays.asList(
                "North Pole","Lapland","Korvatunturi mountain","Finland"
            )),
            new Question("What Makes Santa&#39;s Reindeer Fly?", Arrays.asList(
                "Magical Reindeer Dust","Fusion","Amanita muscaria","Elves"
            )),
            new Question("Including Rudolph, how many reindeer hooves are there?", Arrays.asList(
                "36","24","16","8"
            )),
            new Question("Santa only has one female reindeer, Which one is it?", Arrays.asList(
                "Vixen","Clarice","Cupid","Cupid"
            )),
            new Question("In the 1964 classic Rudolph The Red Nosed Reindeer, what was the snowman narrators name?", Arrays.asList(
                "Sam","Frosty","Burl","Snowy"
            )),
            new Question("What was Rudolph&#39;s father&#39;s name?", Arrays.asList(
                "Donner","Dasher","Blixen","Comet"
            )),
            new Question("In the 1964 movie, What was the name of the coach of the Reindeer Games?", Arrays.asList(
                "Comet","Blixen","Donner","Dasher"
            )),
            new Question("In the 1964 movie, what is the name of the deer that Rudolph befriends at the reindeer games?", Arrays.asList(
                "Fireball","Clarice","Jumper","Vixen"
            )),
            new Question("In the 1964 movie, How did Donner, Rudolph&#39;s father, try to hide Rudolph&#39;s nose?", Arrays.asList(
                "Black mud","Bag","Pillow case","Sock"
            )),
            new Question("In the 1964 movie, what does the Misfit Elf want to be instead of a Santa Elf?", Arrays.asList(
                "Dentist","Reindeer","Toy maker","Candlestick maker"
            )),
            new Question("In the 1964 movie,what was the Bumble&#39;s one weakness?", Arrays.asList(
                "Could not swim","Always hungry","Candy canes","Cross eyed"
            )),
            new Question("In the 1964 movie, what is Yukon Cornelius really in search of?", Arrays.asList(
                "Peppermint","Gold","India","Polar Bears"
            )),
            new Question("In the 1964 movie, why is the train on the Island of Misfit Toys?", Arrays.asList(
                "Square wheels","No Engine","Paint does not match","It does not toot"
            )),
            new Question("In the 1964 movie, what is the name of the Jack in the Box?", Arrays.asList(
                "Charlie","Sam","Billy","Jack"
            )),
            new Question("In the 1964 movie, why did Santa Claus almost cancel Christmas?", Arrays.asList(
                "Storm","No snow","No toys","The Reindeer were sick"
            )),
            new Question("In the 1964 movie, what animal noise did the elf make to distract the Bumble?", Arrays.asList(
                "Oink","Growl","Bark","Meow"
            )),
            new Question("In the 1964 movie, what is the name of the prospector?", Arrays.asList(
                "Yukon Cornelius","Slider Sam","Bumble","Jack"
            )),
            new Question("How far do reindeer travel when they migrate?", Arrays.asList(
                "3000 miles","700 miles","500 miles","0 miles"
            )),
            new Question("How fast can a reindeer run?", Arrays.asList(
                "48 miles per hour","17 miles per hour","19 miles per hour","14 miles per hour","52 miles per hour","41 miles per hour"
            ))
            )
        );

        Trivia enusTrivia = new Trivia();
        enusTrivia.setLocale(Locale.US);
        enusTrivia.setQuestions(Arrays.asList(
            new Question("Reindeer have very thick coats, how many hairs per square inch do they have?", Arrays.asList(
                "13,000","1,200","5,000","700","1,000","120,000"
            )),
            new Question("The 1964 classic Rudolph The Red Nosed Reindeer was filmed in? ", Arrays.asList(
                "Japan","United States","Finland","Germany","Canada","Norway","France"
            )),
            new Question("Santas reindeer are cared for by one of the Christmas elves, what is his name?", Arrays.asList(
                "Wunorse Openslae","Alabaster Snowball","Bushy Evergreen","Pepper Minstix"
            )),
            new Question("If all of Santas reindeer had antlers while pulling his Christmas sleigh, they would all be", Arrays.asList(
                "Girls","Boys","Girls and boys","No way to tell"
            )),
            new Question("What do Reindeer eat?", Arrays.asList(
                "Lichen","Grasses","Leaves","Berries"
            )),
            new Question("What of the following is not true?", Arrays.asList(
                "Caribou live on all continents","Both reindeer and Caribou are the same species","Caribou are bigger than reindeer","Reindeer live in Scandinavia and Russia"
            )),
            new Question("In what year did Rudolph make his television debut?", Arrays.asList(
                "1964","1979","2000","1956"
            )),
            new Question("Who was the voice of Rudolph in the 1964 classic?", Arrays.asList(
                "Billie Mae Richards","Burl Ives","Paul Soles","Lady Gaga"
            )),
            new Question("In 1939 what retailer used the story of Rudolph the Red Nose Reindeer?", Arrays.asList(
                "Montgomery Ward","Sears","Macys","Kmart"
            )),
            new Question("Santa&#39;s reindeer named Donner was originally named what?", Arrays.asList(
                "Dunder","Donny","Dweedle","Dreamy"
            )),
            new Question("Who invented the story of Rudolph?", Arrays.asList(
                "Robert May","Johnny Marks","Santa","J K  Rowling"
            )),
            new Question("In what location will you not find reindeer?", Arrays.asList(
                "North Pole","Lapland","Korvatunturi mountain","Finland"
            )),
            new Question("What Makes Santa&#39;s Reindeer Fly?", Arrays.asList(
                "Magical Reindeer Dust","Fusion","Amanita muscaria","Elves"
            )),
            new Question("Including Rudolph, how many reindeer hooves are there?", Arrays.asList(
                "36","24","16","8"
            )),
            new Question("Santa only has one female reindeer, Which one is it?", Arrays.asList(
                "Vixen","Clarice","Cupid","Cupid"
            )),
            new Question("In the 1964 classic Rudolph The Red Nosed Reindeer, what was the snowman narrators name?", Arrays.asList(
                "Sam","Frosty","Burl","Snowy"
            )),
            new Question("What was Rudolph&#39;s father&#39;s name?", Arrays.asList(
                "Donner","Dasher","Blixen","Comet"
            )),
            new Question("In the 1964 movie, What was the name of the coach of the Reindeer Games?", Arrays.asList(
                "Comet","Blixen","Donner","Dasher"
            )),
            new Question("In the 1964 movie, what is the name of the deer that Rudolph befriends at the reindeer games?", Arrays.asList(
                "Fireball","Clarice","Jumper","Vixen"
            )),
            new Question("In the 1964 movie, How did Donner, Rudolph&#39;s father, try to hide Rudolph&#39;s nose?", Arrays.asList(
                "Black mud","Bag","Pillow case","Sock"
            )),
            new Question("In the 1964 movie, what does the Misfit Elf want to be instead of a Santa Elf?", Arrays.asList(
                "Dentist","Reindeer","Toy maker","Candlestick maker"
            )),
            new Question("In the 1964 movie,what was the Bumble&#39;s one weakness?", Arrays.asList(
                "Could not swim","Always hungry","Candy canes","Cross eyed"
            )),
            new Question("In the 1964 movie, what is Yukon Cornelius really in search of?", Arrays.asList(
                "Peppermint","Gold","India","Polar Bears"
            )),
            new Question("In the 1964 movie, why is the train on the Island of Misfit Toys?", Arrays.asList(
                "Square wheels","No Engine","Paint does not match","It does not toot"
            )),
            new Question("In the 1964 movie, what is the name of the Jack in the Box?", Arrays.asList(
                "Charlie","Sam","Billy","Jack"
            )),
            new Question("In the 1964 movie, why did Santa Claus almost cancel Christmas?", Arrays.asList(
                "Storm","No snow","No toys","The Reindeer were sick"
            )),
            new Question("In the 1964 movie, what animal noise did the elf make to distract the Bumble?", Arrays.asList(
                "Oink","Growl","Bark","Meow"
            )),
            new Question("In the 1964 movie, what is the name of the prospector?", Arrays.asList(
                "Yukon Cornelius","Slider Sam","Bumble","Jack"
            )),
            new Question("How far do reindeer travel when they migrate?", Arrays.asList(
                "3000 miles","700 miles","500 miles","0 miles"
            )),
            new Question("How fast can a reindeer run?", Arrays.asList(
                "48 miles per hour","17 miles per hour","19 miles per hour","14 miles per hour","52 miles per hour","41 miles per hour"
            ))
            )
        );

        Trivia dedeTrivia = new Trivia();
        dedeTrivia.setLocale(Locale.GERMANY);
        dedeTrivia.setQuestions(Arrays.asList(
            new Question("Rentiere haben ein sehr dickes Fell, Wie viele Haare pro Quadratzentimeter haben sie?", Arrays.asList(
                "13,000","1,200","5,000","700","1,000","120,000"
            )),
            new Question("Der Klassiker aus dem Jahr 1964, Rudolph mit der roten Nase, wurde gedreht in? ", Arrays.asList(
                "Japan","USA","Finnland","Deutschland","Kanada","Norwegen","Frankreich"
            )),
            new Question("Um die Rentiere des Weihnachtsmanns kümmert sich eine der Weihnachtselfen, Wie heißt sie?", Arrays.asList(
                "Wunorse Openslae","Alabaster Snowball","Bushy Evergreen","Pfeffer Minstix"
            )),
            new Question("Wenn alle Rentiere des Weihnachtsmanns Geweihe hätten, während sie seinen Weihnachtsschlitten ziehen, wären sie alle ", Arrays.asList(
                "Weiblich","Männlich","Weiblich und männlich","Kann man nicht sagen"
            )),
            new Question("Was essen Rentiere?", Arrays.asList(
                "Flechten","Gras","Blätter","Beeren"
            )),
            new Question("Welche Aussage ist nicht richtig?", Arrays.asList(
                "Karibus leben auf allen Kontinenten","Karibus und Rentiere gehören derselben Gattung an ","Karibus sind größer als Rentiere","Rentiere leben in Skandinavien und Russland"
            )),
            new Question("In welchem Jahr kam Rudolph ins Fernsehen?", Arrays.asList(
                "1964","1979","2000","1956"
            )),
            new Question("Wer war der Sprecher für Rudolph im klassischen Film aus dem Jahr 1964?", Arrays.asList(
                "Billie Mae Richards","Burl Ives","Paul Soles","Lady Gaga"
            )),
            new Question("Welche Handelskette verwendete 1939 die Geschichte von Rudolph mit der roten Nase?", Arrays.asList(
                "Montgomery Ward","Sears","Macys","Kmart"
            )),
            new Question("Wie hieß das Rentier des Weihnachtsmanns namens Donner ursprünglich?", Arrays.asList(
                "Dunder","Donny","Dweedle","Dreamy"
            )),
            new Question("Wer hat die Geschichte von Rudolph erfunden?", Arrays.asList(
                "Robert May","Johnny Marks","Santa","J K  Rowling"
            )),
            new Question("Wo findest du keine Rentiere?", Arrays.asList(
                "Nordpol","Lappland","Korvatunturi Berge","Finnland"
            )),
            new Question("Warum können die Rentiere des Weihnachtsmanns fliegen?", Arrays.asList(
                "Magischer Staub der Rentiere","Fusion","Amanita muscaria","Elfen"
            )),
            new Question("Wieviele Rentierhufe gibt es hier einschließlich Rudolph?", Arrays.asList(
                "36","24","16","8"
            )),
            new Question("Der Weihnachtsmann hat nur ein weibliches Rentier, Wie heißt es?", Arrays.asList(
                "Blitzen","Clarice","Cupid","Cupid"
            )),
            new Question("Wie war der Name des erzählenden Schneemanns im klassischen Film Rudolph mit der roten Nase aus dem Jahr 1964?", Arrays.asList(
                "Sam","Frosty","Burl","Snowy"
            )),
            new Question("Wie hieß der Vater von Rudolph?", Arrays.asList(
                "Donner","Dasher","Blixen","Comet"
            )),
            new Question("Wie war der Name des Trainers der Rentierspiele im klassischen Film aus dem Jahr 1964?", Arrays.asList(
                "Comet","Blixen","Donner","Dasher"
            )),
            new Question("Wie war im klassichen Film aus 1964 der Name des Hirsches, mit dem sich Rudolph befreundete?", Arrays.asList(
                "Fireball","Clarice","Jumper","Vixen"
            )),
            new Question("Wie hat der Vater von Rudolph, Donner, im Film aus dem Jahr 1964 versucht, die Nase von Rudolph zu verbergen?", Arrays.asList(
                "Schwarzer Schlamm","Sack","Kissenbezug","Socke"
            )),
            new Question("Was möchte die Misfit Elfe im Film aus dem Jahr 1964 werden anstatt eine Elfe für den Weihnachtsmann?", Arrays.asList(
                "Zahnarzt","Rentier","Spielzeugmacher","Kerzenmacher"
            )),
            new Question("Was war die einzige Schwäche von Bumble im Film aus dem Jahr 1964?", Arrays.asList(
                "Konnte nicht schwimmen","War immer hungrig","Zuckerstangen","Schielte"
            )),
            new Question("Was sucht Yukon Cornelius in Wirklichkeit im Film aus dem Jahr 1964?", Arrays.asList(
                "Pfefferminz","Gold","Indien","Polarbären"
            )),
            new Question("Warum befindet sich der Zug im Film aus dem Jahr 1964 auf der Insel des fehlerhaften Spielzeugs?", Arrays.asList(
                "Viereckige Räder","Keine Dampfmaschine","Farbe stimmt nicht","Pfeift nicht"
            )),
            new Question("Wie lautet der Name des Schachtelmännchens im Film aus dem Jahr 1964?", Arrays.asList(
                "Charlie","Sam","Billy","Jack"
            )),
            new Question("Warum hat der Weihnachtsmann im Film aus dem Jahr 1964 Weihnachten beinahe abgesagt?", Arrays.asList(
                "Sturm","Kein Schnee","Kein Spielzeug","Die Rentiere waren krank"
            )),
            new Question("Welches tierische Geräusch machte die Elfe im Film aus dem Jahr 1964, um den Bumble abzulenken?", Arrays.asList(
                "Oink","Knurr","Wauwau","Miau"
            )),
            new Question("Wie lautet der Name des Goldsuchers im Film aus dem Jahr 1964?", Arrays.asList(
                "Yukon Cornelius","Slider Sam","Bumble","Jack"
            )),
            new Question("Wie weit ziehen Rentiere auf ihren Wanderungen?", Arrays.asList(
                "4800 km","1100 km","800 km","0 km"
            )),
            new Question("Wie schnell läuft ein Rentier?", Arrays.asList(
                "77 km pro Stunde","27 km pro Stunde","30 km pro Stunde","22 km pro Stunde","83 km pro Stunde","65 km pro Stunde"
            ))
            )
        );

        return Arrays.asList(
            engbTrivia,
            enusTrivia,
            dedeTrivia
        );
    }
}
