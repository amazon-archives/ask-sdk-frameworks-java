package com.amazon.ask.interaction.mapper;

import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.Utils;
import com.amazon.ask.interaction.definition.Model;
import com.amazon.ask.interaction.types.slot.BaseSlotValue;
import com.amazon.ask.interaction.types.slot.list.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BasicSlotTypeParserTest {
    private static Slot slot(String value) {
        return Slot.builder().withName("test").withValue(value).build();
    }

    private <T extends BaseSlotValue> void testBasicListType(Class<T> clazz, Slot slot) throws IntentParseException {
        T expected = Utils.instantiate(clazz);
        expected.setSlot(slot);
        IntentMapper intentMapper = IntentMapper.fromModel(Model.empty());
        assertEquals(expected, intentMapper.slotReaderFor(clazz).read(null, slot));
    }

    private static class IllegalAccessSlot extends BaseSlotValue {
        private IllegalAccessSlot(Slot slot) {
            this.setSlot(slot);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAccessException() throws IntentParseException {
        testBasicListType(IllegalAccessSlot.class, slot("test"));
    }

    @Test
    public void testActor() throws IntentParseException {
        testBasicListType(Actor.class, slot("test"));
    }

    @Test
    public void testAdministrativeArea() throws IntentParseException {
        testBasicListType(AdministrativeArea.class, slot("test"));
    }

    @Test
    public void testAggregateRating() throws IntentParseException {
        testBasicListType(AggregateRating.class, slot("test"));
    }

    @Test
    public void testAirline() throws IntentParseException {
        testBasicListType(Airline.class, slot("test"));
    }

    @Test
    public void testAirport() throws IntentParseException {
        testBasicListType(Airport.class, slot("test"));
    }

    @Test
    public void testAnimal() throws IntentParseException {
        testBasicListType(Animal.class, slot("test"));
    }

    @Test
    public void testArtist() throws IntentParseException {
        testBasicListType(Artist.class, slot("test"));
    }

    @Test
    public void testAthlete() throws IntentParseException {
        testBasicListType(Athlete.class, slot("test"));
    }

    @Test
    public void testATCity() throws IntentParseException {
        testBasicListType(ATCity.class, slot("test"));
    }

    @Test
    public void testATRegion() throws IntentParseException {
        testBasicListType(ATRegion.class, slot("test"));
    }

    @Test
    public void testAuthor() throws IntentParseException {
        testBasicListType(Author.class, slot("test"));
    }

    @Test
    public void testBook() throws IntentParseException {
        testBasicListType(Book.class, slot("test"));
    }

    @Test
    public void testBookSeries() throws IntentParseException {
        testBasicListType(BookSeries.class, slot("test"));
    }

    @Test
    public void testBroadcastChannel() throws IntentParseException {
        testBasicListType(BroadcastChannel.class, slot("test"));
    }

    @Test
    public void testCivicStructure() throws IntentParseException {
        testBasicListType(CivicStructure.class, slot("test"));
    }

    @Test
    public void testColor() throws IntentParseException {
        testBasicListType(Color.class, slot("test"));
    }

    @Test
    public void testComic() throws IntentParseException {
        testBasicListType(Comic.class, slot("test"));
    }

    @Test
    public void testCorporation() throws IntentParseException {
        testBasicListType(Corporation.class, slot("test"));
    }

    @Test
    public void testCountry() throws IntentParseException {
        testBasicListType(Country.class, slot("test"));
    }

    @Test
    public void testCreativeWorkType() throws IntentParseException {
        testBasicListType(CreativeWorkType.class, slot("test"));
    }

    @Test
    public void testDessert() throws IntentParseException {
        testBasicListType(Dessert.class, slot("test"));
    }

    @Test
    public void testDeviceType() throws IntentParseException {
        testBasicListType(DeviceType.class, slot("test"));
    }

    @Test
    public void testDECity() throws IntentParseException {
        testBasicListType(DECity.class, slot("test"));
    }

    @Test
    public void testDEFirstName() throws IntentParseException {
        testBasicListType(DEFirstName.class, slot("test"));
    }

    @Test
    public void testDERegion() throws IntentParseException {
        testBasicListType(DERegion.class, slot("test"));
    }

    @Test
    public void testDirector() throws IntentParseException {
        testBasicListType(Director.class, slot("test"));
    }

    @Test
    public void testDrink() throws IntentParseException {
        testBasicListType(Drink.class, slot("test"));
    }

    @Test
    public void testEducationalOrganization() throws IntentParseException {
        testBasicListType(EducationalOrganization.class, slot("test"));
    }

    @Test
    public void testEventType() throws IntentParseException {
        testBasicListType(EventType.class, slot("test"));
    }

    @Test
    public void testEUCity() throws IntentParseException {
        testBasicListType(EUCity.class, slot("test"));
    }

    @Test
    public void testFestival() throws IntentParseException {
        testBasicListType(Festival.class, slot("test"));
    }

    @Test
    public void testFictionalCharacter() throws IntentParseException {
        testBasicListType(FictionalCharacter.class, slot("test"));
    }

    @Test
    public void testFinancialService() throws IntentParseException {
        testBasicListType(FinancialService.class, slot("test"));
    }

    @Test
    public void testFood() throws IntentParseException {
        testBasicListType(Food.class, slot("test"));
    }

    @Test
    public void testFoodEstablishment() throws IntentParseException {
        testBasicListType(FoodEstablishment.class, slot("test"));
    }

    @Test
    public void testGame() throws IntentParseException {
        testBasicListType(Game.class, slot("test"));
    }

    @Test
    public void testGBCity() throws IntentParseException {
        testBasicListType(GBCity.class, slot("test"));
    }

    @Test
    public void testGBFirstName() throws IntentParseException {
        testBasicListType(GBFirstName.class, slot("test"));
    }

    @Test
    public void testGBRegion() throws IntentParseException {
        testBasicListType(GBRegion.class, slot("test"));
    }

    @Test
    public void testGenre() throws IntentParseException {
        testBasicListType(Genre.class, slot("test"));
    }

    @Test
    public void testLandform() throws IntentParseException {
        testBasicListType(Landform.class, slot("test"));
    }

    @Test
    public void testLandmarksOrHistoricalBuildings() throws IntentParseException {
        testBasicListType(LandmarksOrHistoricalBuildings.class, slot("test"));
    }

    @Test
    public void testLanguage() throws IntentParseException {
        testBasicListType(Language.class, slot("test"));
    }

    @Test
    public void testLocalBusiness() throws IntentParseException {
        testBasicListType(LocalBusiness.class, slot("test"));
    }

    @Test
    public void testLocalBusinessType() throws IntentParseException {
        testBasicListType(LocalBusinessType.class, slot("test"));
    }

    @Test
    public void testMedicalOrganization() throws IntentParseException {
        testBasicListType(MedicalOrganization.class, slot("test"));
    }

    @Test
    public void testMonth() throws IntentParseException {
        testBasicListType(Month.class, slot("test"));
    }

    @Test
    public void testMovie() throws IntentParseException {
        testBasicListType(Movie.class, slot("test"));
    }

    @Test
    public void testMovieSeries() throws IntentParseException {
        testBasicListType(MovieSeries.class, slot("test"));
    }

    @Test
    public void testMovieTheater() throws IntentParseException {
        testBasicListType(MovieTheater.class, slot("test"));
    }

    @Test
    public void testMusicAlbum() throws IntentParseException {
        testBasicListType(MusicAlbum.class, slot("test"));
    }

    @Test
    public void testMusicCreativeWorkType() throws IntentParseException {
        testBasicListType(MusicCreativeWorkType.class, slot("test"));
    }

    @Test
    public void testMusicEvent() throws IntentParseException {
        testBasicListType(MusicEvent.class, slot("test"));
    }

    @Test
    public void testMusicGroup() throws IntentParseException {
        testBasicListType(MusicGroup.class, slot("test"));
    }

    @Test
    public void testMusician() throws IntentParseException {
        testBasicListType(Musician.class, slot("test"));
    }

    @Test
    public void testMusicPlaylist() throws IntentParseException {
        testBasicListType(MusicPlaylist.class, slot("test"));
    }

    @Test
    public void testMusicRecording() throws IntentParseException {
        testBasicListType(MusicRecording.class, slot("test"));
    }

    @Test
    public void testMusicVenue() throws IntentParseException {
        testBasicListType(MusicVenue.class, slot("test"));
    }

    @Test
    public void testMusicVideo() throws IntentParseException {
        testBasicListType(MusicVideo.class, slot("test"));
    }

    @Test
    public void testOrganization() throws IntentParseException {
        testBasicListType(Organization.class, slot("test"));
    }

    @Test
    public void testPerson() throws IntentParseException {
        testBasicListType(Person.class, slot("test"));
    }

    @Test
    public void testPostalAddress() throws IntentParseException {
        testBasicListType(PostalAddress.class, slot("test"));
    }

    @Test
    public void testProfessional() throws IntentParseException {
        testBasicListType(Professional.class, slot("test"));
    }

    @Test
    public void testProfessionalType() throws IntentParseException {
        testBasicListType(ProfessionalType.class, slot("test"));
    }

    @Test
    public void testRadioChannel() throws IntentParseException {
        testBasicListType(RadioChannel.class, slot("test"));
    }

    @Test
    public void testResidence() throws IntentParseException {
        testBasicListType(Residence.class, slot("test"));
    }

    @Test
    public void testRoom() throws IntentParseException {
        testBasicListType(Room.class, slot("test"));
    }

    @Test
    public void testScreeningEvent() throws IntentParseException {
        testBasicListType(ScreeningEvent.class, slot("test"));
    }

    @Test
    public void testService() throws IntentParseException {
        testBasicListType(Service.class, slot("test"));
    }

    @Test
    public void testSocialMediaPlatform() throws IntentParseException {
        testBasicListType(SocialMediaPlatform.class, slot("test"));
    }

    @Test
    public void testSoftwareApplication() throws IntentParseException {
        testBasicListType(SoftwareApplication.class, slot("test"));
    }

    @Test
    public void testSoftwareGame() throws IntentParseException {
        testBasicListType(SoftwareGame.class, slot("test"));
    }

    @Test
    public void testSoftwareGenre() throws IntentParseException {
        testBasicListType(SoftwareGenre.class, slot("test"));
    }

    @Test
    public void testSport() throws IntentParseException {
        testBasicListType(Sport.class, slot("test"));
    }

    @Test
    public void testSportsEvent() throws IntentParseException {
        testBasicListType(SportsEvent.class, slot("test"));
    }

    @Test
    public void testSportsTeam() throws IntentParseException {
        testBasicListType(SportsTeam.class, slot("test"));
    }

    @Test
    public void testStreetAddress() throws IntentParseException {
        testBasicListType(StreetAddress.class, slot("test"));
    }

    @Test
    public void testTelevisionChannel() throws IntentParseException {
        testBasicListType(TelevisionChannel.class, slot("test"));
    }

    @Test
    public void testTVEpisode() throws IntentParseException {
        testBasicListType(TVEpisode.class, slot("test"));
    }

    @Test
    public void testTVSeason() throws IntentParseException {
        testBasicListType(TVSeason.class, slot("test"));
    }

    @Test
    public void testTVSeries() throws IntentParseException {
        testBasicListType(TVSeries.class, slot("test"));
    }

    @Test
    public void testUSCity() throws IntentParseException {
        testBasicListType(USCity.class, slot("test"));
    }

    @Test
    public void testUSFirstName() throws IntentParseException {
        testBasicListType(USFirstName.class, slot("test"));
    }

    @Test
    public void testUSState() throws IntentParseException {
        testBasicListType(USState.class, slot("test"));
    }

    @Test
    public void testVideoGame() throws IntentParseException {
        testBasicListType(VideoGame.class, slot("test"));
    }

    @Test
    public void testWeatherCondition() throws IntentParseException {
        testBasicListType(WeatherCondition.class, slot("test"));
    }

    @Test
    public void testWrittenCreativeWorkType() throws IntentParseException {
        testBasicListType(WrittenCreativeWorkType.class, slot("test"));
    }
}
