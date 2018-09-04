package com.amazon.ask.interaction.codegen;

import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.types.slot.PhoneNumber;
import com.amazon.ask.interaction.types.intent.*;
import com.amazon.ask.interaction.types.slot.*;
import com.amazon.ask.interaction.types.slot.date.AmazonDate;
import com.amazon.ask.interaction.types.slot.list.*;
import com.amazon.ask.interaction.types.slot.time.AmazonTime;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TypeNames {
    public static TypeName get(String typename) {
        return TYPES.get(typename);
    }

    private static void addType(Class<?> clazz) {
        SlotType slot = clazz.getAnnotation(SlotType.class);
        Intent intent = clazz.getAnnotation(Intent.class);

        String name = slot == null ? intent.value() : slot.value();

        TYPES.put(name, ClassName.get(clazz));
    }

    private static final Map<String, TypeName> TYPES = new HashMap<>();
    static {
        // Intents
        addType(CancelIntent.class);
        addType(FallbackIntent.class);
        addType(HelpIntent.class);
        addType(LoopOffIntent.class);
        addType(LoopOnIntent.class);
        addType(MoreIntent.class);
        addType(NavigateHomeIntent.class);
        addType(NavigateSettingsIntent.class);
        addType(NextIntent.class);
        addType(NoIntent.class);
        addType(PageDownIntent.class);
        addType(PageUpIntent.class);
        addType(PauseIntent.class);
        addType(PreviousIntent.class);
        addType(RepeatIntent.class);
        addType(ResumeIntent.class);
        addType(ScrollDownIntent.class);
        addType(ScrollUpIntent.class);
        addType(ScrollLeftIntent.class);
        addType(ScrollRightIntent.class);
        addType(ShuffleOffIntent.class);
        addType(ShuffleOnIntent.class);
        addType(StartOverIntent.class);
        addType(StopIntent.class);
        addType(YesIntent.class);

        // Slots
        addType(AmazonLiteral.class);
        addType(AmazonNumber.class);
        addType(AmazonDate.class);
        addType(AmazonDuration.class);
        addType(AmazonTime.class);
        addType(FourDigitNumber.class);
        addType(Actor.class);
        addType(AdministrativeArea.class);
        addType(AggregateRating.class);
        addType(Airline.class);
        addType(Airport.class);
        addType(Animal.class);
        addType(Artist.class);
        addType(Athlete.class);
        addType(ATCity.class);
        addType(ATRegion.class);
        addType(Author.class);
        addType(Book.class);
        addType(BookSeries.class);
        addType(BroadcastChannel.class);
        addType(CivicStructure.class);
        addType(Color.class);
        addType(Comic.class);
        addType(Corporation.class);
        addType(Country.class);
        addType(CreativeWorkType.class);
        addType(DayOfWeek.class);
        addType(Dessert.class);
        addType(DeviceType.class);
        addType(DECity.class);
        addType(DEFirstName.class);
        addType(DERegion.class);
        addType(Director.class);
        addType(Drink.class);
        addType(EducationalOrganization.class);
        addType(EventType.class);
        addType(EUCity.class);
        addType(Festival.class);
        addType(FictionalCharacter.class);
        addType(FinancialService.class);
        addType(Food.class);
        addType(FoodEstablishment.class);
        addType(Game.class);
        addType(GBCity.class);
        addType(GBFirstName.class);
        addType(GBRegion.class);
        addType(Genre.class);
        addType(Landform.class);
        addType(LandmarksOrHistoricalBuildings.class);
        addType(Language.class);
        addType(LocalBusiness.class);
        addType(LocalBusinessType.class);
        addType(MedicalOrganization.class);
        addType(Month.class);
        addType(Movie.class);
        addType(MovieSeries.class);
        addType(MovieTheater.class);
        addType(MusicAlbum.class);
        addType(MusicCreativeWorkType.class);
        addType(MusicEvent.class);
        addType(MusicGroup.class);
        addType(Musician.class);
        addType(MusicPlaylist.class);
        addType(MusicRecording.class);
        addType(MusicVenue.class);
        addType(MusicVideo.class);
        addType(Organization.class);
        addType(Person.class);
        addType(PhoneNumber.class);
        addType(PostalAddress.class);
        addType(Professional.class);
        addType(ProfessionalType.class);
        addType(RadioChannel.class);
        addType(Residence.class);
        addType(Room.class);
        addType(ScreeningEvent.class);
        addType(SearchQuery.class);
        addType(Service.class);
        addType(SocialMediaPlatform.class);
        addType(SoftwareApplication.class);
        addType(SoftwareGame.class);
        addType(SoftwareGenre.class);
        addType(Sport.class);
        addType(SportsEvent.class);
        addType(SportsTeam.class);
        addType(StreetAddress.class);
        addType(TelevisionChannel.class);
        addType(TVEpisode.class);
        addType(TVSeason.class);
        addType(TVSeries.class);
        addType(USCity.class);
        addType(USFirstName.class);
        addType(USState.class);
        addType(VideoGame.class);
        addType(WeatherCondition.class);
        addType(WrittenCreativeWorkType.class);
    }
}
