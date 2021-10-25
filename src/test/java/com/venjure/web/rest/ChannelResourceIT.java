package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Channel;
import com.venjure.domain.Customer;
import com.venjure.domain.Facet;
import com.venjure.domain.FacetValue;
import com.venjure.domain.Jorder;
import com.venjure.domain.PaymentMethod;
import com.venjure.domain.Product;
import com.venjure.domain.ProductVariant;
import com.venjure.domain.Promotion;
import com.venjure.domain.ShippingMethod;
import com.venjure.domain.Zone;
import com.venjure.repository.ChannelRepository;
import com.venjure.service.ChannelService;
import com.venjure.service.criteria.ChannelCriteria;
import com.venjure.service.dto.ChannelDTO;
import com.venjure.service.mapper.ChannelMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ChannelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ChannelResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULTLANGUAGECODE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULTLANGUAGECODE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCYCODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCYCODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PRICESINCLUDETAX = false;
    private static final Boolean UPDATED_PRICESINCLUDETAX = true;

    private static final String ENTITY_API_URL = "/api/channels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChannelRepository channelRepository;

    @Mock
    private ChannelRepository channelRepositoryMock;

    @Autowired
    private ChannelMapper channelMapper;

    @Mock
    private ChannelService channelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChannelMockMvc;

    private Channel channel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Channel createEntity(EntityManager em) {
        Channel channel = new Channel()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .code(DEFAULT_CODE)
            .token(DEFAULT_TOKEN)
            .defaultlanguagecode(DEFAULT_DEFAULTLANGUAGECODE)
            .currencycode(DEFAULT_CURRENCYCODE)
            .pricesincludetax(DEFAULT_PRICESINCLUDETAX);
        return channel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Channel createUpdatedEntity(EntityManager em) {
        Channel channel = new Channel()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .code(UPDATED_CODE)
            .token(UPDATED_TOKEN)
            .defaultlanguagecode(UPDATED_DEFAULTLANGUAGECODE)
            .currencycode(UPDATED_CURRENCYCODE)
            .pricesincludetax(UPDATED_PRICESINCLUDETAX);
        return channel;
    }

    @BeforeEach
    public void initTest() {
        channel = createEntity(em);
    }

    @Test
    @Transactional
    void createChannel() throws Exception {
        int databaseSizeBeforeCreate = channelRepository.findAll().size();
        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);
        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isCreated());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeCreate + 1);
        Channel testChannel = channelList.get(channelList.size() - 1);
        assertThat(testChannel.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testChannel.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testChannel.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChannel.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testChannel.getDefaultlanguagecode()).isEqualTo(DEFAULT_DEFAULTLANGUAGECODE);
        assertThat(testChannel.getCurrencycode()).isEqualTo(DEFAULT_CURRENCYCODE);
        assertThat(testChannel.getPricesincludetax()).isEqualTo(DEFAULT_PRICESINCLUDETAX);
    }

    @Test
    @Transactional
    void createChannelWithExistingId() throws Exception {
        // Create the Channel with an existing ID
        channel.setId(1L);
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        int databaseSizeBeforeCreate = channelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setCreatedat(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setUpdatedat(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setCode(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setToken(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDefaultlanguagecodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setDefaultlanguagecode(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencycodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setCurrencycode(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricesincludetaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = channelRepository.findAll().size();
        // set the field null
        channel.setPricesincludetax(null);

        // Create the Channel, which fails.
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        restChannelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isBadRequest());

        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChannels() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList
        restChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channel.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].defaultlanguagecode").value(hasItem(DEFAULT_DEFAULTLANGUAGECODE)))
            .andExpect(jsonPath("$.[*].currencycode").value(hasItem(DEFAULT_CURRENCYCODE)))
            .andExpect(jsonPath("$.[*].pricesincludetax").value(hasItem(DEFAULT_PRICESINCLUDETAX.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChannelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(channelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChannelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(channelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllChannelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(channelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restChannelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(channelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get the channel
        restChannelMockMvc
            .perform(get(ENTITY_API_URL_ID, channel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(channel.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.defaultlanguagecode").value(DEFAULT_DEFAULTLANGUAGECODE))
            .andExpect(jsonPath("$.currencycode").value(DEFAULT_CURRENCYCODE))
            .andExpect(jsonPath("$.pricesincludetax").value(DEFAULT_PRICESINCLUDETAX.booleanValue()));
    }

    @Test
    @Transactional
    void getChannelsByIdFiltering() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        Long id = channel.getId();

        defaultChannelShouldBeFound("id.equals=" + id);
        defaultChannelShouldNotBeFound("id.notEquals=" + id);

        defaultChannelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultChannelShouldNotBeFound("id.greaterThan=" + id);

        defaultChannelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultChannelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChannelsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where createdat equals to DEFAULT_CREATEDAT
        defaultChannelShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the channelList where createdat equals to UPDATED_CREATEDAT
        defaultChannelShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllChannelsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where createdat not equals to DEFAULT_CREATEDAT
        defaultChannelShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the channelList where createdat not equals to UPDATED_CREATEDAT
        defaultChannelShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllChannelsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultChannelShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the channelList where createdat equals to UPDATED_CREATEDAT
        defaultChannelShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllChannelsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where createdat is not null
        defaultChannelShouldBeFound("createdat.specified=true");

        // Get all the channelList where createdat is null
        defaultChannelShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where updatedat equals to DEFAULT_UPDATEDAT
        defaultChannelShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the channelList where updatedat equals to UPDATED_UPDATEDAT
        defaultChannelShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllChannelsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultChannelShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the channelList where updatedat not equals to UPDATED_UPDATEDAT
        defaultChannelShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllChannelsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultChannelShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the channelList where updatedat equals to UPDATED_UPDATEDAT
        defaultChannelShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllChannelsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where updatedat is not null
        defaultChannelShouldBeFound("updatedat.specified=true");

        // Get all the channelList where updatedat is null
        defaultChannelShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where code equals to DEFAULT_CODE
        defaultChannelShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the channelList where code equals to UPDATED_CODE
        defaultChannelShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where code not equals to DEFAULT_CODE
        defaultChannelShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the channelList where code not equals to UPDATED_CODE
        defaultChannelShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where code in DEFAULT_CODE or UPDATED_CODE
        defaultChannelShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the channelList where code equals to UPDATED_CODE
        defaultChannelShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where code is not null
        defaultChannelShouldBeFound("code.specified=true");

        // Get all the channelList where code is null
        defaultChannelShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelsByCodeContainsSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where code contains DEFAULT_CODE
        defaultChannelShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the channelList where code contains UPDATED_CODE
        defaultChannelShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where code does not contain DEFAULT_CODE
        defaultChannelShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the channelList where code does not contain UPDATED_CODE
        defaultChannelShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChannelsByTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where token equals to DEFAULT_TOKEN
        defaultChannelShouldBeFound("token.equals=" + DEFAULT_TOKEN);

        // Get all the channelList where token equals to UPDATED_TOKEN
        defaultChannelShouldNotBeFound("token.equals=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllChannelsByTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where token not equals to DEFAULT_TOKEN
        defaultChannelShouldNotBeFound("token.notEquals=" + DEFAULT_TOKEN);

        // Get all the channelList where token not equals to UPDATED_TOKEN
        defaultChannelShouldBeFound("token.notEquals=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllChannelsByTokenIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where token in DEFAULT_TOKEN or UPDATED_TOKEN
        defaultChannelShouldBeFound("token.in=" + DEFAULT_TOKEN + "," + UPDATED_TOKEN);

        // Get all the channelList where token equals to UPDATED_TOKEN
        defaultChannelShouldNotBeFound("token.in=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllChannelsByTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where token is not null
        defaultChannelShouldBeFound("token.specified=true");

        // Get all the channelList where token is null
        defaultChannelShouldNotBeFound("token.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelsByTokenContainsSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where token contains DEFAULT_TOKEN
        defaultChannelShouldBeFound("token.contains=" + DEFAULT_TOKEN);

        // Get all the channelList where token contains UPDATED_TOKEN
        defaultChannelShouldNotBeFound("token.contains=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllChannelsByTokenNotContainsSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where token does not contain DEFAULT_TOKEN
        defaultChannelShouldNotBeFound("token.doesNotContain=" + DEFAULT_TOKEN);

        // Get all the channelList where token does not contain UPDATED_TOKEN
        defaultChannelShouldBeFound("token.doesNotContain=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    void getAllChannelsByDefaultlanguagecodeIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where defaultlanguagecode equals to DEFAULT_DEFAULTLANGUAGECODE
        defaultChannelShouldBeFound("defaultlanguagecode.equals=" + DEFAULT_DEFAULTLANGUAGECODE);

        // Get all the channelList where defaultlanguagecode equals to UPDATED_DEFAULTLANGUAGECODE
        defaultChannelShouldNotBeFound("defaultlanguagecode.equals=" + UPDATED_DEFAULTLANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllChannelsByDefaultlanguagecodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where defaultlanguagecode not equals to DEFAULT_DEFAULTLANGUAGECODE
        defaultChannelShouldNotBeFound("defaultlanguagecode.notEquals=" + DEFAULT_DEFAULTLANGUAGECODE);

        // Get all the channelList where defaultlanguagecode not equals to UPDATED_DEFAULTLANGUAGECODE
        defaultChannelShouldBeFound("defaultlanguagecode.notEquals=" + UPDATED_DEFAULTLANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllChannelsByDefaultlanguagecodeIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where defaultlanguagecode in DEFAULT_DEFAULTLANGUAGECODE or UPDATED_DEFAULTLANGUAGECODE
        defaultChannelShouldBeFound("defaultlanguagecode.in=" + DEFAULT_DEFAULTLANGUAGECODE + "," + UPDATED_DEFAULTLANGUAGECODE);

        // Get all the channelList where defaultlanguagecode equals to UPDATED_DEFAULTLANGUAGECODE
        defaultChannelShouldNotBeFound("defaultlanguagecode.in=" + UPDATED_DEFAULTLANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllChannelsByDefaultlanguagecodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where defaultlanguagecode is not null
        defaultChannelShouldBeFound("defaultlanguagecode.specified=true");

        // Get all the channelList where defaultlanguagecode is null
        defaultChannelShouldNotBeFound("defaultlanguagecode.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelsByDefaultlanguagecodeContainsSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where defaultlanguagecode contains DEFAULT_DEFAULTLANGUAGECODE
        defaultChannelShouldBeFound("defaultlanguagecode.contains=" + DEFAULT_DEFAULTLANGUAGECODE);

        // Get all the channelList where defaultlanguagecode contains UPDATED_DEFAULTLANGUAGECODE
        defaultChannelShouldNotBeFound("defaultlanguagecode.contains=" + UPDATED_DEFAULTLANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllChannelsByDefaultlanguagecodeNotContainsSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where defaultlanguagecode does not contain DEFAULT_DEFAULTLANGUAGECODE
        defaultChannelShouldNotBeFound("defaultlanguagecode.doesNotContain=" + DEFAULT_DEFAULTLANGUAGECODE);

        // Get all the channelList where defaultlanguagecode does not contain UPDATED_DEFAULTLANGUAGECODE
        defaultChannelShouldBeFound("defaultlanguagecode.doesNotContain=" + UPDATED_DEFAULTLANGUAGECODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCurrencycodeIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where currencycode equals to DEFAULT_CURRENCYCODE
        defaultChannelShouldBeFound("currencycode.equals=" + DEFAULT_CURRENCYCODE);

        // Get all the channelList where currencycode equals to UPDATED_CURRENCYCODE
        defaultChannelShouldNotBeFound("currencycode.equals=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCurrencycodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where currencycode not equals to DEFAULT_CURRENCYCODE
        defaultChannelShouldNotBeFound("currencycode.notEquals=" + DEFAULT_CURRENCYCODE);

        // Get all the channelList where currencycode not equals to UPDATED_CURRENCYCODE
        defaultChannelShouldBeFound("currencycode.notEquals=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCurrencycodeIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where currencycode in DEFAULT_CURRENCYCODE or UPDATED_CURRENCYCODE
        defaultChannelShouldBeFound("currencycode.in=" + DEFAULT_CURRENCYCODE + "," + UPDATED_CURRENCYCODE);

        // Get all the channelList where currencycode equals to UPDATED_CURRENCYCODE
        defaultChannelShouldNotBeFound("currencycode.in=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCurrencycodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where currencycode is not null
        defaultChannelShouldBeFound("currencycode.specified=true");

        // Get all the channelList where currencycode is null
        defaultChannelShouldNotBeFound("currencycode.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelsByCurrencycodeContainsSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where currencycode contains DEFAULT_CURRENCYCODE
        defaultChannelShouldBeFound("currencycode.contains=" + DEFAULT_CURRENCYCODE);

        // Get all the channelList where currencycode contains UPDATED_CURRENCYCODE
        defaultChannelShouldNotBeFound("currencycode.contains=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllChannelsByCurrencycodeNotContainsSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where currencycode does not contain DEFAULT_CURRENCYCODE
        defaultChannelShouldNotBeFound("currencycode.doesNotContain=" + DEFAULT_CURRENCYCODE);

        // Get all the channelList where currencycode does not contain UPDATED_CURRENCYCODE
        defaultChannelShouldBeFound("currencycode.doesNotContain=" + UPDATED_CURRENCYCODE);
    }

    @Test
    @Transactional
    void getAllChannelsByPricesincludetaxIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where pricesincludetax equals to DEFAULT_PRICESINCLUDETAX
        defaultChannelShouldBeFound("pricesincludetax.equals=" + DEFAULT_PRICESINCLUDETAX);

        // Get all the channelList where pricesincludetax equals to UPDATED_PRICESINCLUDETAX
        defaultChannelShouldNotBeFound("pricesincludetax.equals=" + UPDATED_PRICESINCLUDETAX);
    }

    @Test
    @Transactional
    void getAllChannelsByPricesincludetaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where pricesincludetax not equals to DEFAULT_PRICESINCLUDETAX
        defaultChannelShouldNotBeFound("pricesincludetax.notEquals=" + DEFAULT_PRICESINCLUDETAX);

        // Get all the channelList where pricesincludetax not equals to UPDATED_PRICESINCLUDETAX
        defaultChannelShouldBeFound("pricesincludetax.notEquals=" + UPDATED_PRICESINCLUDETAX);
    }

    @Test
    @Transactional
    void getAllChannelsByPricesincludetaxIsInShouldWork() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where pricesincludetax in DEFAULT_PRICESINCLUDETAX or UPDATED_PRICESINCLUDETAX
        defaultChannelShouldBeFound("pricesincludetax.in=" + DEFAULT_PRICESINCLUDETAX + "," + UPDATED_PRICESINCLUDETAX);

        // Get all the channelList where pricesincludetax equals to UPDATED_PRICESINCLUDETAX
        defaultChannelShouldNotBeFound("pricesincludetax.in=" + UPDATED_PRICESINCLUDETAX);
    }

    @Test
    @Transactional
    void getAllChannelsByPricesincludetaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        // Get all the channelList where pricesincludetax is not null
        defaultChannelShouldBeFound("pricesincludetax.specified=true");

        // Get all the channelList where pricesincludetax is null
        defaultChannelShouldNotBeFound("pricesincludetax.specified=false");
    }

    @Test
    @Transactional
    void getAllChannelsByDefaulttaxzoneIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        Zone defaulttaxzone = ZoneResourceIT.createEntity(em);
        em.persist(defaulttaxzone);
        em.flush();
        channel.setDefaulttaxzone(defaulttaxzone);
        channelRepository.saveAndFlush(channel);
        Long defaulttaxzoneId = defaulttaxzone.getId();

        // Get all the channelList where defaulttaxzone equals to defaulttaxzoneId
        defaultChannelShouldBeFound("defaulttaxzoneId.equals=" + defaulttaxzoneId);

        // Get all the channelList where defaulttaxzone equals to (defaulttaxzoneId + 1)
        defaultChannelShouldNotBeFound("defaulttaxzoneId.equals=" + (defaulttaxzoneId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByDefaultshippingzoneIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        Zone defaultshippingzone = ZoneResourceIT.createEntity(em);
        em.persist(defaultshippingzone);
        em.flush();
        channel.setDefaultshippingzone(defaultshippingzone);
        channelRepository.saveAndFlush(channel);
        Long defaultshippingzoneId = defaultshippingzone.getId();

        // Get all the channelList where defaultshippingzone equals to defaultshippingzoneId
        defaultChannelShouldBeFound("defaultshippingzoneId.equals=" + defaultshippingzoneId);

        // Get all the channelList where defaultshippingzone equals to (defaultshippingzoneId + 1)
        defaultChannelShouldNotBeFound("defaultshippingzoneId.equals=" + (defaultshippingzoneId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        PaymentMethod paymentMethod = PaymentMethodResourceIT.createEntity(em);
        em.persist(paymentMethod);
        em.flush();
        channel.addPaymentMethod(paymentMethod);
        channelRepository.saveAndFlush(channel);
        Long paymentMethodId = paymentMethod.getId();

        // Get all the channelList where paymentMethod equals to paymentMethodId
        defaultChannelShouldBeFound("paymentMethodId.equals=" + paymentMethodId);

        // Get all the channelList where paymentMethod equals to (paymentMethodId + 1)
        defaultChannelShouldNotBeFound("paymentMethodId.equals=" + (paymentMethodId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        Product product = ProductResourceIT.createEntity(em);
        em.persist(product);
        em.flush();
        channel.addProduct(product);
        channelRepository.saveAndFlush(channel);
        Long productId = product.getId();

        // Get all the channelList where product equals to productId
        defaultChannelShouldBeFound("productId.equals=" + productId);

        // Get all the channelList where product equals to (productId + 1)
        defaultChannelShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByPromotionIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        Promotion promotion = PromotionResourceIT.createEntity(em);
        em.persist(promotion);
        em.flush();
        channel.addPromotion(promotion);
        channelRepository.saveAndFlush(channel);
        Long promotionId = promotion.getId();

        // Get all the channelList where promotion equals to promotionId
        defaultChannelShouldBeFound("promotionId.equals=" + promotionId);

        // Get all the channelList where promotion equals to (promotionId + 1)
        defaultChannelShouldNotBeFound("promotionId.equals=" + (promotionId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByShippingMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        ShippingMethod shippingMethod = ShippingMethodResourceIT.createEntity(em);
        em.persist(shippingMethod);
        em.flush();
        channel.addShippingMethod(shippingMethod);
        channelRepository.saveAndFlush(channel);
        Long shippingMethodId = shippingMethod.getId();

        // Get all the channelList where shippingMethod equals to shippingMethodId
        defaultChannelShouldBeFound("shippingMethodId.equals=" + shippingMethodId);

        // Get all the channelList where shippingMethod equals to (shippingMethodId + 1)
        defaultChannelShouldNotBeFound("shippingMethodId.equals=" + (shippingMethodId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByCustomerIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        Customer customer = CustomerResourceIT.createEntity(em);
        em.persist(customer);
        em.flush();
        channel.addCustomer(customer);
        channelRepository.saveAndFlush(channel);
        Long customerId = customer.getId();

        // Get all the channelList where customer equals to customerId
        defaultChannelShouldBeFound("customerId.equals=" + customerId);

        // Get all the channelList where customer equals to (customerId + 1)
        defaultChannelShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByFacetIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        Facet facet = FacetResourceIT.createEntity(em);
        em.persist(facet);
        em.flush();
        channel.addFacet(facet);
        channelRepository.saveAndFlush(channel);
        Long facetId = facet.getId();

        // Get all the channelList where facet equals to facetId
        defaultChannelShouldBeFound("facetId.equals=" + facetId);

        // Get all the channelList where facet equals to (facetId + 1)
        defaultChannelShouldNotBeFound("facetId.equals=" + (facetId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByFacetValueIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        FacetValue facetValue = FacetValueResourceIT.createEntity(em);
        em.persist(facetValue);
        em.flush();
        channel.addFacetValue(facetValue);
        channelRepository.saveAndFlush(channel);
        Long facetValueId = facetValue.getId();

        // Get all the channelList where facetValue equals to facetValueId
        defaultChannelShouldBeFound("facetValueId.equals=" + facetValueId);

        // Get all the channelList where facetValue equals to (facetValueId + 1)
        defaultChannelShouldNotBeFound("facetValueId.equals=" + (facetValueId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByJorderIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        Jorder jorder = JorderResourceIT.createEntity(em);
        em.persist(jorder);
        em.flush();
        channel.addJorder(jorder);
        channelRepository.saveAndFlush(channel);
        Long jorderId = jorder.getId();

        // Get all the channelList where jorder equals to jorderId
        defaultChannelShouldBeFound("jorderId.equals=" + jorderId);

        // Get all the channelList where jorder equals to (jorderId + 1)
        defaultChannelShouldNotBeFound("jorderId.equals=" + (jorderId + 1));
    }

    @Test
    @Transactional
    void getAllChannelsByProductVariantIsEqualToSomething() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);
        ProductVariant productVariant = ProductVariantResourceIT.createEntity(em);
        em.persist(productVariant);
        em.flush();
        channel.addProductVariant(productVariant);
        channelRepository.saveAndFlush(channel);
        Long productVariantId = productVariant.getId();

        // Get all the channelList where productVariant equals to productVariantId
        defaultChannelShouldBeFound("productVariantId.equals=" + productVariantId);

        // Get all the channelList where productVariant equals to (productVariantId + 1)
        defaultChannelShouldNotBeFound("productVariantId.equals=" + (productVariantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChannelShouldBeFound(String filter) throws Exception {
        restChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channel.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].defaultlanguagecode").value(hasItem(DEFAULT_DEFAULTLANGUAGECODE)))
            .andExpect(jsonPath("$.[*].currencycode").value(hasItem(DEFAULT_CURRENCYCODE)))
            .andExpect(jsonPath("$.[*].pricesincludetax").value(hasItem(DEFAULT_PRICESINCLUDETAX.booleanValue())));

        // Check, that the count call also returns 1
        restChannelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChannelShouldNotBeFound(String filter) throws Exception {
        restChannelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChannelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChannel() throws Exception {
        // Get the channel
        restChannelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        int databaseSizeBeforeUpdate = channelRepository.findAll().size();

        // Update the channel
        Channel updatedChannel = channelRepository.findById(channel.getId()).get();
        // Disconnect from session so that the updates on updatedChannel are not directly saved in db
        em.detach(updatedChannel);
        updatedChannel
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .code(UPDATED_CODE)
            .token(UPDATED_TOKEN)
            .defaultlanguagecode(UPDATED_DEFAULTLANGUAGECODE)
            .currencycode(UPDATED_CURRENCYCODE)
            .pricesincludetax(UPDATED_PRICESINCLUDETAX);
        ChannelDTO channelDTO = channelMapper.toDto(updatedChannel);

        restChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, channelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(channelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
        Channel testChannel = channelList.get(channelList.size() - 1);
        assertThat(testChannel.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testChannel.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testChannel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChannel.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testChannel.getDefaultlanguagecode()).isEqualTo(UPDATED_DEFAULTLANGUAGECODE);
        assertThat(testChannel.getCurrencycode()).isEqualTo(UPDATED_CURRENCYCODE);
        assertThat(testChannel.getPricesincludetax()).isEqualTo(UPDATED_PRICESINCLUDETAX);
    }

    @Test
    @Transactional
    void putNonExistingChannel() throws Exception {
        int databaseSizeBeforeUpdate = channelRepository.findAll().size();
        channel.setId(count.incrementAndGet());

        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, channelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(channelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChannel() throws Exception {
        int databaseSizeBeforeUpdate = channelRepository.findAll().size();
        channel.setId(count.incrementAndGet());

        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(channelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChannel() throws Exception {
        int databaseSizeBeforeUpdate = channelRepository.findAll().size();
        channel.setId(count.incrementAndGet());

        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(channelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChannelWithPatch() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        int databaseSizeBeforeUpdate = channelRepository.findAll().size();

        // Update the channel using partial update
        Channel partialUpdatedChannel = new Channel();
        partialUpdatedChannel.setId(channel.getId());

        partialUpdatedChannel.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).defaultlanguagecode(UPDATED_DEFAULTLANGUAGECODE);

        restChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChannel))
            )
            .andExpect(status().isOk());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
        Channel testChannel = channelList.get(channelList.size() - 1);
        assertThat(testChannel.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testChannel.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testChannel.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testChannel.getToken()).isEqualTo(DEFAULT_TOKEN);
        assertThat(testChannel.getDefaultlanguagecode()).isEqualTo(UPDATED_DEFAULTLANGUAGECODE);
        assertThat(testChannel.getCurrencycode()).isEqualTo(DEFAULT_CURRENCYCODE);
        assertThat(testChannel.getPricesincludetax()).isEqualTo(DEFAULT_PRICESINCLUDETAX);
    }

    @Test
    @Transactional
    void fullUpdateChannelWithPatch() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        int databaseSizeBeforeUpdate = channelRepository.findAll().size();

        // Update the channel using partial update
        Channel partialUpdatedChannel = new Channel();
        partialUpdatedChannel.setId(channel.getId());

        partialUpdatedChannel
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .code(UPDATED_CODE)
            .token(UPDATED_TOKEN)
            .defaultlanguagecode(UPDATED_DEFAULTLANGUAGECODE)
            .currencycode(UPDATED_CURRENCYCODE)
            .pricesincludetax(UPDATED_PRICESINCLUDETAX);

        restChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChannel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChannel))
            )
            .andExpect(status().isOk());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
        Channel testChannel = channelList.get(channelList.size() - 1);
        assertThat(testChannel.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testChannel.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testChannel.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testChannel.getToken()).isEqualTo(UPDATED_TOKEN);
        assertThat(testChannel.getDefaultlanguagecode()).isEqualTo(UPDATED_DEFAULTLANGUAGECODE);
        assertThat(testChannel.getCurrencycode()).isEqualTo(UPDATED_CURRENCYCODE);
        assertThat(testChannel.getPricesincludetax()).isEqualTo(UPDATED_PRICESINCLUDETAX);
    }

    @Test
    @Transactional
    void patchNonExistingChannel() throws Exception {
        int databaseSizeBeforeUpdate = channelRepository.findAll().size();
        channel.setId(count.incrementAndGet());

        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, channelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(channelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChannel() throws Exception {
        int databaseSizeBeforeUpdate = channelRepository.findAll().size();
        channel.setId(count.incrementAndGet());

        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(channelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChannel() throws Exception {
        int databaseSizeBeforeUpdate = channelRepository.findAll().size();
        channel.setId(count.incrementAndGet());

        // Create the Channel
        ChannelDTO channelDTO = channelMapper.toDto(channel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(channelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChannel() throws Exception {
        // Initialize the database
        channelRepository.saveAndFlush(channel);

        int databaseSizeBeforeDelete = channelRepository.findAll().size();

        // Delete the channel
        restChannelMockMvc
            .perform(delete(ENTITY_API_URL_ID, channel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
