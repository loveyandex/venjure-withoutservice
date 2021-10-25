package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Asset;
import com.venjure.repository.AssetRepository;
import com.venjure.service.criteria.AssetCriteria;
import com.venjure.service.dto.AssetDTO;
import com.venjure.service.mapper.AssetMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssetResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MIMETYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIMETYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;
    private static final Integer SMALLER_WIDTH = 1 - 1;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;
    private static final Integer SMALLER_HEIGHT = 1 - 1;

    private static final Integer DEFAULT_FILESIZE = 1;
    private static final Integer UPDATED_FILESIZE = 2;
    private static final Integer SMALLER_FILESIZE = 1 - 1;

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIEW = "AAAAAAAAAA";
    private static final String UPDATED_PREVIEW = "BBBBBBBBBB";

    private static final String DEFAULT_FOCALPOINT = "AAAAAAAAAA";
    private static final String UPDATED_FOCALPOINT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetMockMvc;

    private Asset asset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createEntity(EntityManager em) {
        Asset asset = new Asset()
            .createdat(DEFAULT_CREATEDAT)
            .updatedat(DEFAULT_UPDATEDAT)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .mimetype(DEFAULT_MIMETYPE)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .filesize(DEFAULT_FILESIZE)
            .source(DEFAULT_SOURCE)
            .preview(DEFAULT_PREVIEW)
            .focalpoint(DEFAULT_FOCALPOINT);
        return asset;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createUpdatedEntity(EntityManager em) {
        Asset asset = new Asset()
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .mimetype(UPDATED_MIMETYPE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .filesize(UPDATED_FILESIZE)
            .source(UPDATED_SOURCE)
            .preview(UPDATED_PREVIEW)
            .focalpoint(UPDATED_FOCALPOINT);
        return asset;
    }

    @BeforeEach
    public void initTest() {
        asset = createEntity(em);
    }

    @Test
    @Transactional
    void createAsset() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().size();
        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);
        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isCreated());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate + 1);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testAsset.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testAsset.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAsset.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAsset.getMimetype()).isEqualTo(DEFAULT_MIMETYPE);
        assertThat(testAsset.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testAsset.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testAsset.getFilesize()).isEqualTo(DEFAULT_FILESIZE);
        assertThat(testAsset.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testAsset.getPreview()).isEqualTo(DEFAULT_PREVIEW);
        assertThat(testAsset.getFocalpoint()).isEqualTo(DEFAULT_FOCALPOINT);
    }

    @Test
    @Transactional
    void createAssetWithExistingId() throws Exception {
        // Create the Asset with an existing ID
        asset.setId(1L);
        AssetDTO assetDTO = assetMapper.toDto(asset);

        int databaseSizeBeforeCreate = assetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setCreatedat(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setUpdatedat(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setName(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setType(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMimetypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setMimetype(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWidthIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setWidth(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setHeight(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilesizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setFilesize(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setSource(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPreviewIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setPreview(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.toDto(asset);

        restAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isBadRequest());

        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssets() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].mimetype").value(hasItem(DEFAULT_MIMETYPE)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].filesize").value(hasItem(DEFAULT_FILESIZE)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].preview").value(hasItem(DEFAULT_PREVIEW)))
            .andExpect(jsonPath("$.[*].focalpoint").value(hasItem(DEFAULT_FOCALPOINT)));
    }

    @Test
    @Transactional
    void getAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get the asset
        restAssetMockMvc
            .perform(get(ENTITY_API_URL_ID, asset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asset.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.mimetype").value(DEFAULT_MIMETYPE))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.filesize").value(DEFAULT_FILESIZE))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.preview").value(DEFAULT_PREVIEW))
            .andExpect(jsonPath("$.focalpoint").value(DEFAULT_FOCALPOINT));
    }

    @Test
    @Transactional
    void getAssetsByIdFiltering() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        Long id = asset.getId();

        defaultAssetShouldBeFound("id.equals=" + id);
        defaultAssetShouldNotBeFound("id.notEquals=" + id);

        defaultAssetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where createdat equals to DEFAULT_CREATEDAT
        defaultAssetShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the assetList where createdat equals to UPDATED_CREATEDAT
        defaultAssetShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAssetsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where createdat not equals to DEFAULT_CREATEDAT
        defaultAssetShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the assetList where createdat not equals to UPDATED_CREATEDAT
        defaultAssetShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAssetsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultAssetShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the assetList where createdat equals to UPDATED_CREATEDAT
        defaultAssetShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllAssetsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where createdat is not null
        defaultAssetShouldBeFound("createdat.specified=true");

        // Get all the assetList where createdat is null
        defaultAssetShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where updatedat equals to DEFAULT_UPDATEDAT
        defaultAssetShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the assetList where updatedat equals to UPDATED_UPDATEDAT
        defaultAssetShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAssetsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultAssetShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the assetList where updatedat not equals to UPDATED_UPDATEDAT
        defaultAssetShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAssetsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultAssetShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the assetList where updatedat equals to UPDATED_UPDATEDAT
        defaultAssetShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllAssetsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where updatedat is not null
        defaultAssetShouldBeFound("updatedat.specified=true");

        // Get all the assetList where updatedat is null
        defaultAssetShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where name equals to DEFAULT_NAME
        defaultAssetShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the assetList where name equals to UPDATED_NAME
        defaultAssetShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAssetsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where name not equals to DEFAULT_NAME
        defaultAssetShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the assetList where name not equals to UPDATED_NAME
        defaultAssetShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAssetsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAssetShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the assetList where name equals to UPDATED_NAME
        defaultAssetShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAssetsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where name is not null
        defaultAssetShouldBeFound("name.specified=true");

        // Get all the assetList where name is null
        defaultAssetShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByNameContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where name contains DEFAULT_NAME
        defaultAssetShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the assetList where name contains UPDATED_NAME
        defaultAssetShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAssetsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where name does not contain DEFAULT_NAME
        defaultAssetShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the assetList where name does not contain UPDATED_NAME
        defaultAssetShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAssetsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where type equals to DEFAULT_TYPE
        defaultAssetShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the assetList where type equals to UPDATED_TYPE
        defaultAssetShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where type not equals to DEFAULT_TYPE
        defaultAssetShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the assetList where type not equals to UPDATED_TYPE
        defaultAssetShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAssetShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the assetList where type equals to UPDATED_TYPE
        defaultAssetShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where type is not null
        defaultAssetShouldBeFound("type.specified=true");

        // Get all the assetList where type is null
        defaultAssetShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByTypeContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where type contains DEFAULT_TYPE
        defaultAssetShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the assetList where type contains UPDATED_TYPE
        defaultAssetShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where type does not contain DEFAULT_TYPE
        defaultAssetShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the assetList where type does not contain UPDATED_TYPE
        defaultAssetShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByMimetypeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where mimetype equals to DEFAULT_MIMETYPE
        defaultAssetShouldBeFound("mimetype.equals=" + DEFAULT_MIMETYPE);

        // Get all the assetList where mimetype equals to UPDATED_MIMETYPE
        defaultAssetShouldNotBeFound("mimetype.equals=" + UPDATED_MIMETYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByMimetypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where mimetype not equals to DEFAULT_MIMETYPE
        defaultAssetShouldNotBeFound("mimetype.notEquals=" + DEFAULT_MIMETYPE);

        // Get all the assetList where mimetype not equals to UPDATED_MIMETYPE
        defaultAssetShouldBeFound("mimetype.notEquals=" + UPDATED_MIMETYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByMimetypeIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where mimetype in DEFAULT_MIMETYPE or UPDATED_MIMETYPE
        defaultAssetShouldBeFound("mimetype.in=" + DEFAULT_MIMETYPE + "," + UPDATED_MIMETYPE);

        // Get all the assetList where mimetype equals to UPDATED_MIMETYPE
        defaultAssetShouldNotBeFound("mimetype.in=" + UPDATED_MIMETYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByMimetypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where mimetype is not null
        defaultAssetShouldBeFound("mimetype.specified=true");

        // Get all the assetList where mimetype is null
        defaultAssetShouldNotBeFound("mimetype.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByMimetypeContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where mimetype contains DEFAULT_MIMETYPE
        defaultAssetShouldBeFound("mimetype.contains=" + DEFAULT_MIMETYPE);

        // Get all the assetList where mimetype contains UPDATED_MIMETYPE
        defaultAssetShouldNotBeFound("mimetype.contains=" + UPDATED_MIMETYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByMimetypeNotContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where mimetype does not contain DEFAULT_MIMETYPE
        defaultAssetShouldNotBeFound("mimetype.doesNotContain=" + DEFAULT_MIMETYPE);

        // Get all the assetList where mimetype does not contain UPDATED_MIMETYPE
        defaultAssetShouldBeFound("mimetype.doesNotContain=" + UPDATED_MIMETYPE);
    }

    @Test
    @Transactional
    void getAllAssetsByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where width equals to DEFAULT_WIDTH
        defaultAssetShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the assetList where width equals to UPDATED_WIDTH
        defaultAssetShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAssetsByWidthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where width not equals to DEFAULT_WIDTH
        defaultAssetShouldNotBeFound("width.notEquals=" + DEFAULT_WIDTH);

        // Get all the assetList where width not equals to UPDATED_WIDTH
        defaultAssetShouldBeFound("width.notEquals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAssetsByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultAssetShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the assetList where width equals to UPDATED_WIDTH
        defaultAssetShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAssetsByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where width is not null
        defaultAssetShouldBeFound("width.specified=true");

        // Get all the assetList where width is null
        defaultAssetShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where width is greater than or equal to DEFAULT_WIDTH
        defaultAssetShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the assetList where width is greater than or equal to UPDATED_WIDTH
        defaultAssetShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAssetsByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where width is less than or equal to DEFAULT_WIDTH
        defaultAssetShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the assetList where width is less than or equal to SMALLER_WIDTH
        defaultAssetShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllAssetsByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where width is less than DEFAULT_WIDTH
        defaultAssetShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the assetList where width is less than UPDATED_WIDTH
        defaultAssetShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllAssetsByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where width is greater than DEFAULT_WIDTH
        defaultAssetShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the assetList where width is greater than SMALLER_WIDTH
        defaultAssetShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllAssetsByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where height equals to DEFAULT_HEIGHT
        defaultAssetShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the assetList where height equals to UPDATED_HEIGHT
        defaultAssetShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAssetsByHeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where height not equals to DEFAULT_HEIGHT
        defaultAssetShouldNotBeFound("height.notEquals=" + DEFAULT_HEIGHT);

        // Get all the assetList where height not equals to UPDATED_HEIGHT
        defaultAssetShouldBeFound("height.notEquals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAssetsByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultAssetShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the assetList where height equals to UPDATED_HEIGHT
        defaultAssetShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAssetsByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where height is not null
        defaultAssetShouldBeFound("height.specified=true");

        // Get all the assetList where height is null
        defaultAssetShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where height is greater than or equal to DEFAULT_HEIGHT
        defaultAssetShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the assetList where height is greater than or equal to UPDATED_HEIGHT
        defaultAssetShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAssetsByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where height is less than or equal to DEFAULT_HEIGHT
        defaultAssetShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the assetList where height is less than or equal to SMALLER_HEIGHT
        defaultAssetShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAssetsByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where height is less than DEFAULT_HEIGHT
        defaultAssetShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the assetList where height is less than UPDATED_HEIGHT
        defaultAssetShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAssetsByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where height is greater than DEFAULT_HEIGHT
        defaultAssetShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the assetList where height is greater than SMALLER_HEIGHT
        defaultAssetShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllAssetsByFilesizeIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where filesize equals to DEFAULT_FILESIZE
        defaultAssetShouldBeFound("filesize.equals=" + DEFAULT_FILESIZE);

        // Get all the assetList where filesize equals to UPDATED_FILESIZE
        defaultAssetShouldNotBeFound("filesize.equals=" + UPDATED_FILESIZE);
    }

    @Test
    @Transactional
    void getAllAssetsByFilesizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where filesize not equals to DEFAULT_FILESIZE
        defaultAssetShouldNotBeFound("filesize.notEquals=" + DEFAULT_FILESIZE);

        // Get all the assetList where filesize not equals to UPDATED_FILESIZE
        defaultAssetShouldBeFound("filesize.notEquals=" + UPDATED_FILESIZE);
    }

    @Test
    @Transactional
    void getAllAssetsByFilesizeIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where filesize in DEFAULT_FILESIZE or UPDATED_FILESIZE
        defaultAssetShouldBeFound("filesize.in=" + DEFAULT_FILESIZE + "," + UPDATED_FILESIZE);

        // Get all the assetList where filesize equals to UPDATED_FILESIZE
        defaultAssetShouldNotBeFound("filesize.in=" + UPDATED_FILESIZE);
    }

    @Test
    @Transactional
    void getAllAssetsByFilesizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where filesize is not null
        defaultAssetShouldBeFound("filesize.specified=true");

        // Get all the assetList where filesize is null
        defaultAssetShouldNotBeFound("filesize.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByFilesizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where filesize is greater than or equal to DEFAULT_FILESIZE
        defaultAssetShouldBeFound("filesize.greaterThanOrEqual=" + DEFAULT_FILESIZE);

        // Get all the assetList where filesize is greater than or equal to UPDATED_FILESIZE
        defaultAssetShouldNotBeFound("filesize.greaterThanOrEqual=" + UPDATED_FILESIZE);
    }

    @Test
    @Transactional
    void getAllAssetsByFilesizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where filesize is less than or equal to DEFAULT_FILESIZE
        defaultAssetShouldBeFound("filesize.lessThanOrEqual=" + DEFAULT_FILESIZE);

        // Get all the assetList where filesize is less than or equal to SMALLER_FILESIZE
        defaultAssetShouldNotBeFound("filesize.lessThanOrEqual=" + SMALLER_FILESIZE);
    }

    @Test
    @Transactional
    void getAllAssetsByFilesizeIsLessThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where filesize is less than DEFAULT_FILESIZE
        defaultAssetShouldNotBeFound("filesize.lessThan=" + DEFAULT_FILESIZE);

        // Get all the assetList where filesize is less than UPDATED_FILESIZE
        defaultAssetShouldBeFound("filesize.lessThan=" + UPDATED_FILESIZE);
    }

    @Test
    @Transactional
    void getAllAssetsByFilesizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where filesize is greater than DEFAULT_FILESIZE
        defaultAssetShouldNotBeFound("filesize.greaterThan=" + DEFAULT_FILESIZE);

        // Get all the assetList where filesize is greater than SMALLER_FILESIZE
        defaultAssetShouldBeFound("filesize.greaterThan=" + SMALLER_FILESIZE);
    }

    @Test
    @Transactional
    void getAllAssetsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where source equals to DEFAULT_SOURCE
        defaultAssetShouldBeFound("source.equals=" + DEFAULT_SOURCE);

        // Get all the assetList where source equals to UPDATED_SOURCE
        defaultAssetShouldNotBeFound("source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAssetsBySourceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where source not equals to DEFAULT_SOURCE
        defaultAssetShouldNotBeFound("source.notEquals=" + DEFAULT_SOURCE);

        // Get all the assetList where source not equals to UPDATED_SOURCE
        defaultAssetShouldBeFound("source.notEquals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAssetsBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where source in DEFAULT_SOURCE or UPDATED_SOURCE
        defaultAssetShouldBeFound("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE);

        // Get all the assetList where source equals to UPDATED_SOURCE
        defaultAssetShouldNotBeFound("source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAssetsBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where source is not null
        defaultAssetShouldBeFound("source.specified=true");

        // Get all the assetList where source is null
        defaultAssetShouldNotBeFound("source.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsBySourceContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where source contains DEFAULT_SOURCE
        defaultAssetShouldBeFound("source.contains=" + DEFAULT_SOURCE);

        // Get all the assetList where source contains UPDATED_SOURCE
        defaultAssetShouldNotBeFound("source.contains=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAssetsBySourceNotContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where source does not contain DEFAULT_SOURCE
        defaultAssetShouldNotBeFound("source.doesNotContain=" + DEFAULT_SOURCE);

        // Get all the assetList where source does not contain UPDATED_SOURCE
        defaultAssetShouldBeFound("source.doesNotContain=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllAssetsByPreviewIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where preview equals to DEFAULT_PREVIEW
        defaultAssetShouldBeFound("preview.equals=" + DEFAULT_PREVIEW);

        // Get all the assetList where preview equals to UPDATED_PREVIEW
        defaultAssetShouldNotBeFound("preview.equals=" + UPDATED_PREVIEW);
    }

    @Test
    @Transactional
    void getAllAssetsByPreviewIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where preview not equals to DEFAULT_PREVIEW
        defaultAssetShouldNotBeFound("preview.notEquals=" + DEFAULT_PREVIEW);

        // Get all the assetList where preview not equals to UPDATED_PREVIEW
        defaultAssetShouldBeFound("preview.notEquals=" + UPDATED_PREVIEW);
    }

    @Test
    @Transactional
    void getAllAssetsByPreviewIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where preview in DEFAULT_PREVIEW or UPDATED_PREVIEW
        defaultAssetShouldBeFound("preview.in=" + DEFAULT_PREVIEW + "," + UPDATED_PREVIEW);

        // Get all the assetList where preview equals to UPDATED_PREVIEW
        defaultAssetShouldNotBeFound("preview.in=" + UPDATED_PREVIEW);
    }

    @Test
    @Transactional
    void getAllAssetsByPreviewIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where preview is not null
        defaultAssetShouldBeFound("preview.specified=true");

        // Get all the assetList where preview is null
        defaultAssetShouldNotBeFound("preview.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByPreviewContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where preview contains DEFAULT_PREVIEW
        defaultAssetShouldBeFound("preview.contains=" + DEFAULT_PREVIEW);

        // Get all the assetList where preview contains UPDATED_PREVIEW
        defaultAssetShouldNotBeFound("preview.contains=" + UPDATED_PREVIEW);
    }

    @Test
    @Transactional
    void getAllAssetsByPreviewNotContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where preview does not contain DEFAULT_PREVIEW
        defaultAssetShouldNotBeFound("preview.doesNotContain=" + DEFAULT_PREVIEW);

        // Get all the assetList where preview does not contain UPDATED_PREVIEW
        defaultAssetShouldBeFound("preview.doesNotContain=" + UPDATED_PREVIEW);
    }

    @Test
    @Transactional
    void getAllAssetsByFocalpointIsEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where focalpoint equals to DEFAULT_FOCALPOINT
        defaultAssetShouldBeFound("focalpoint.equals=" + DEFAULT_FOCALPOINT);

        // Get all the assetList where focalpoint equals to UPDATED_FOCALPOINT
        defaultAssetShouldNotBeFound("focalpoint.equals=" + UPDATED_FOCALPOINT);
    }

    @Test
    @Transactional
    void getAllAssetsByFocalpointIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where focalpoint not equals to DEFAULT_FOCALPOINT
        defaultAssetShouldNotBeFound("focalpoint.notEquals=" + DEFAULT_FOCALPOINT);

        // Get all the assetList where focalpoint not equals to UPDATED_FOCALPOINT
        defaultAssetShouldBeFound("focalpoint.notEquals=" + UPDATED_FOCALPOINT);
    }

    @Test
    @Transactional
    void getAllAssetsByFocalpointIsInShouldWork() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where focalpoint in DEFAULT_FOCALPOINT or UPDATED_FOCALPOINT
        defaultAssetShouldBeFound("focalpoint.in=" + DEFAULT_FOCALPOINT + "," + UPDATED_FOCALPOINT);

        // Get all the assetList where focalpoint equals to UPDATED_FOCALPOINT
        defaultAssetShouldNotBeFound("focalpoint.in=" + UPDATED_FOCALPOINT);
    }

    @Test
    @Transactional
    void getAllAssetsByFocalpointIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where focalpoint is not null
        defaultAssetShouldBeFound("focalpoint.specified=true");

        // Get all the assetList where focalpoint is null
        defaultAssetShouldNotBeFound("focalpoint.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetsByFocalpointContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where focalpoint contains DEFAULT_FOCALPOINT
        defaultAssetShouldBeFound("focalpoint.contains=" + DEFAULT_FOCALPOINT);

        // Get all the assetList where focalpoint contains UPDATED_FOCALPOINT
        defaultAssetShouldNotBeFound("focalpoint.contains=" + UPDATED_FOCALPOINT);
    }

    @Test
    @Transactional
    void getAllAssetsByFocalpointNotContainsSomething() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assetList where focalpoint does not contain DEFAULT_FOCALPOINT
        defaultAssetShouldNotBeFound("focalpoint.doesNotContain=" + DEFAULT_FOCALPOINT);

        // Get all the assetList where focalpoint does not contain UPDATED_FOCALPOINT
        defaultAssetShouldBeFound("focalpoint.doesNotContain=" + UPDATED_FOCALPOINT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetShouldBeFound(String filter) throws Exception {
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].mimetype").value(hasItem(DEFAULT_MIMETYPE)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].filesize").value(hasItem(DEFAULT_FILESIZE)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].preview").value(hasItem(DEFAULT_PREVIEW)))
            .andExpect(jsonPath("$.[*].focalpoint").value(hasItem(DEFAULT_FOCALPOINT)));

        // Check, that the count call also returns 1
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetShouldNotBeFound(String filter) throws Exception {
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAsset() throws Exception {
        // Get the asset
        restAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset
        Asset updatedAsset = assetRepository.findById(asset.getId()).get();
        // Disconnect from session so that the updates on updatedAsset are not directly saved in db
        em.detach(updatedAsset);
        updatedAsset
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .mimetype(UPDATED_MIMETYPE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .filesize(UPDATED_FILESIZE)
            .source(UPDATED_SOURCE)
            .preview(UPDATED_PREVIEW)
            .focalpoint(UPDATED_FOCALPOINT);
        AssetDTO assetDTO = assetMapper.toDto(updatedAsset);

        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAsset.getMimetype()).isEqualTo(UPDATED_MIMETYPE);
        assertThat(testAsset.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testAsset.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testAsset.getFilesize()).isEqualTo(UPDATED_FILESIZE);
        assertThat(testAsset.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testAsset.getPreview()).isEqualTo(UPDATED_PREVIEW);
        assertThat(testAsset.getFocalpoint()).isEqualTo(UPDATED_FOCALPOINT);
    }

    @Test
    @Transactional
    void putNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssetWithPatch() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .width(UPDATED_WIDTH)
            .preview(UPDATED_PREVIEW)
            .focalpoint(UPDATED_FOCALPOINT);

        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAsset.getMimetype()).isEqualTo(DEFAULT_MIMETYPE);
        assertThat(testAsset.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testAsset.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testAsset.getFilesize()).isEqualTo(DEFAULT_FILESIZE);
        assertThat(testAsset.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testAsset.getPreview()).isEqualTo(UPDATED_PREVIEW);
        assertThat(testAsset.getFocalpoint()).isEqualTo(UPDATED_FOCALPOINT);
    }

    @Test
    @Transactional
    void fullUpdateAssetWithPatch() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset using partial update
        Asset partialUpdatedAsset = new Asset();
        partialUpdatedAsset.setId(asset.getId());

        partialUpdatedAsset
            .createdat(UPDATED_CREATEDAT)
            .updatedat(UPDATED_UPDATEDAT)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .mimetype(UPDATED_MIMETYPE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .filesize(UPDATED_FILESIZE)
            .source(UPDATED_SOURCE)
            .preview(UPDATED_PREVIEW)
            .focalpoint(UPDATED_FOCALPOINT);

        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsset))
            )
            .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assetList.get(assetList.size() - 1);
        assertThat(testAsset.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testAsset.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAsset.getMimetype()).isEqualTo(UPDATED_MIMETYPE);
        assertThat(testAsset.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testAsset.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testAsset.getFilesize()).isEqualTo(UPDATED_FILESIZE);
        assertThat(testAsset.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testAsset.getPreview()).isEqualTo(UPDATED_PREVIEW);
        assertThat(testAsset.getFocalpoint()).isEqualTo(UPDATED_FOCALPOINT);
    }

    @Test
    @Transactional
    void patchNonExistingAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAsset() throws Exception {
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();
        asset.setId(count.incrementAndGet());

        // Create the Asset
        AssetDTO assetDTO = assetMapper.toDto(asset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Asset in the database
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        int databaseSizeBeforeDelete = assetRepository.findAll().size();

        // Delete the asset
        restAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, asset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Asset> assetList = assetRepository.findAll();
        assertThat(assetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
