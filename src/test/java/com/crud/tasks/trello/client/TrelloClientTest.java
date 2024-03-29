package com.crud.tasks.trello.client;

import com.crud.tasks.domain.CreatedTrelloCard;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.trello.config.TrelloConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TrelloClientTest {

    @InjectMocks
    private TrelloClient trelloClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private TrelloConfig trelloConfig;
    @Before
    public void init(){
        when(trelloConfig.getTrelloApiEndpoint()).thenReturn("http://test.com");
        when(trelloConfig.getTrelloAppKey()).thenReturn("test");
        when(trelloConfig.getTrelloToken()).thenReturn("test");
        when(trelloConfig.getTrelloUsername()).thenReturn("username");
    }
    @Test
    public void shouldFetchTrelloBoards() throws URISyntaxException {
        //Given
        TrelloBoardDto[] trelloBoards = new TrelloBoardDto[1];
        trelloBoards[0] = new TrelloBoardDto("test_board", "test_id", new ArrayList<>());

        URI uri = new URI("http://test.com/members/username/boards?key=test&token=test&fields=name,id&lists=all");

        when(restTemplate.getForObject(uri,TrelloBoardDto[].class)).thenReturn(trelloBoards);

        //When
        List<TrelloBoardDto> fetchedTrelloBoards = trelloClient.getTrelloBoards();

        //Then
        Assert.assertEquals(1,fetchedTrelloBoards.size());
        Assert.assertEquals("test_id",fetchedTrelloBoards.get(0).getId());
        Assert.assertEquals("test_board",fetchedTrelloBoards.get(0).getName());
        Assert.assertEquals(new ArrayList<>(),fetchedTrelloBoards.get(0).getLists());
    }
    @Test
    public void shouldCreateCard() throws URISyntaxException {
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Test task",
                "Test Description", "top","list_id");
        URI uri = new URI("http://test.com/cards?key=test&token=test&name=Test%20task&desc=Test%20Description&pos=top&idList=list_id");

        CreatedTrelloCard createdTrelloCard = new CreatedTrelloCard("1","Task name","http://test.com",null);
        when(restTemplate.postForObject(uri,null,CreatedTrelloCard.class)).thenReturn(createdTrelloCard);
        //When
        CreatedTrelloCard newCard = trelloClient.createTrelloCard(trelloCardDto);
        //Then
        Assert.assertEquals("1",newCard.getId());
        Assert.assertEquals("Task name",newCard.getName());
        Assert.assertEquals("http://test.com",newCard.getShortUrl());
    }
    @Test
    public void shouldReturnEmptyList() throws URISyntaxException {
        //Given
        URI uri = new URI("http://test.com/members/username/boards?key=test&token=test&fields=name,id&lists=all");
        when(restTemplate.getForObject(uri,TrelloBoardDto[].class)).thenReturn(null);

        //When
        List<TrelloBoardDto> fetchedTrelloBoards = trelloClient.getTrelloBoards();

        //Then
        Assert.assertEquals(0,fetchedTrelloBoards.size());
    }
}