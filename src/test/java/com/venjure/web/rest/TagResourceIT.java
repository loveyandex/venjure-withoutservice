package com.venjure.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.venjure.IntegrationTest;
import com.venjure.domain.Tag;
import com.venjure.repository.TagRepository;
import com.venjure.service.criteria.TagCriteria;
import com.venjure.service.dto.TagDTO;
import com.venjure.service.mapper.TagMapper;
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
 * Integration tests for the {@link TagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagResourceIT {

    private static final Instant DEFAULT_CREATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATEDAT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATEDAT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagMockMvc;

    private Tag tag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity(EntityManager em) {
        Tag tag = new Tag().createdat(DEFAULT_CREATEDAT).updatedat(DEFAULT_UPDATEDAT).value(DEFAULT_VALUE);
        return tag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createUpdatedEntity(EntityManager em) {
        Tag tag = new Tag().createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).value(UPDATED_VALUE);
        return tag;
    }

    @BeforeEach
    public void initTest() {
        tag = createEntity(em);
    }

    @Test
    @Transactional
    void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();
        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);
        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getCreatedat()).isEqualTo(DEFAULT_CREATEDAT);
        assertThat(testTag.getUpdatedat()).isEqualTo(DEFAULT_UPDATEDAT);
        assertThat(testTag.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createTagWithExistingId() throws Exception {
        // Create the Tag with an existing ID
        tag.setId(1L);
        TagDTO tagDTO = tagMapper.toDto(tag);

        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setCreatedat(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedatIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setUpdatedat(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setValue(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTags() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get the tag
        restTagMockMvc
            .perform(get(ENTITY_API_URL_ID, tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId().intValue()))
            .andExpect(jsonPath("$.createdat").value(DEFAULT_CREATEDAT.toString()))
            .andExpect(jsonPath("$.updatedat").value(DEFAULT_UPDATEDAT.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getTagsByIdFiltering() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        Long id = tag.getId();

        defaultTagShouldBeFound("id.equals=" + id);
        defaultTagShouldNotBeFound("id.notEquals=" + id);

        defaultTagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTagShouldNotBeFound("id.greaterThan=" + id);

        defaultTagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTagsByCreatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdat equals to DEFAULT_CREATEDAT
        defaultTagShouldBeFound("createdat.equals=" + DEFAULT_CREATEDAT);

        // Get all the tagList where createdat equals to UPDATED_CREATEDAT
        defaultTagShouldNotBeFound("createdat.equals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTagsByCreatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdat not equals to DEFAULT_CREATEDAT
        defaultTagShouldNotBeFound("createdat.notEquals=" + DEFAULT_CREATEDAT);

        // Get all the tagList where createdat not equals to UPDATED_CREATEDAT
        defaultTagShouldBeFound("createdat.notEquals=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTagsByCreatedatIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdat in DEFAULT_CREATEDAT or UPDATED_CREATEDAT
        defaultTagShouldBeFound("createdat.in=" + DEFAULT_CREATEDAT + "," + UPDATED_CREATEDAT);

        // Get all the tagList where createdat equals to UPDATED_CREATEDAT
        defaultTagShouldNotBeFound("createdat.in=" + UPDATED_CREATEDAT);
    }

    @Test
    @Transactional
    void getAllTagsByCreatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdat is not null
        defaultTagShouldBeFound("createdat.specified=true");

        // Get all the tagList where createdat is null
        defaultTagShouldNotBeFound("createdat.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByUpdatedatIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where updatedat equals to DEFAULT_UPDATEDAT
        defaultTagShouldBeFound("updatedat.equals=" + DEFAULT_UPDATEDAT);

        // Get all the tagList where updatedat equals to UPDATED_UPDATEDAT
        defaultTagShouldNotBeFound("updatedat.equals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTagsByUpdatedatIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where updatedat not equals to DEFAULT_UPDATEDAT
        defaultTagShouldNotBeFound("updatedat.notEquals=" + DEFAULT_UPDATEDAT);

        // Get all the tagList where updatedat not equals to UPDATED_UPDATEDAT
        defaultTagShouldBeFound("updatedat.notEquals=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTagsByUpdatedatIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where updatedat in DEFAULT_UPDATEDAT or UPDATED_UPDATEDAT
        defaultTagShouldBeFound("updatedat.in=" + DEFAULT_UPDATEDAT + "," + UPDATED_UPDATEDAT);

        // Get all the tagList where updatedat equals to UPDATED_UPDATEDAT
        defaultTagShouldNotBeFound("updatedat.in=" + UPDATED_UPDATEDAT);
    }

    @Test
    @Transactional
    void getAllTagsByUpdatedatIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where updatedat is not null
        defaultTagShouldBeFound("updatedat.specified=true");

        // Get all the tagList where updatedat is null
        defaultTagShouldNotBeFound("updatedat.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where value equals to DEFAULT_VALUE
        defaultTagShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the tagList where value equals to UPDATED_VALUE
        defaultTagShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTagsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where value not equals to DEFAULT_VALUE
        defaultTagShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the tagList where value not equals to UPDATED_VALUE
        defaultTagShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTagsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultTagShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the tagList where value equals to UPDATED_VALUE
        defaultTagShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTagsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where value is not null
        defaultTagShouldBeFound("value.specified=true");

        // Get all the tagList where value is null
        defaultTagShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByValueContainsSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where value contains DEFAULT_VALUE
        defaultTagShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the tagList where value contains UPDATED_VALUE
        defaultTagShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllTagsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList where value does not contain DEFAULT_VALUE
        defaultTagShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the tagList where value does not contain UPDATED_VALUE
        defaultTagShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTagShouldBeFound(String filter) throws Exception {
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdat").value(hasItem(DEFAULT_CREATEDAT.toString())))
            .andExpect(jsonPath("$.[*].updatedat").value(hasItem(DEFAULT_UPDATEDAT.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTagShouldNotBeFound(String filter) throws Exception {
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag
        Tag updatedTag = tagRepository.findById(tag.getId()).get();
        // Disconnect from session so that the updates on updatedTag are not directly saved in db
        em.detach(updatedTag);
        updatedTag.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).value(UPDATED_VALUE);
        TagDTO tagDTO = tagMapper.toDto(updatedTag);

        restTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTag.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testTag.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagWithPatch() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).value(UPDATED_VALUE);

        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTag.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testTag.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTagWithPatch() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag.createdat(UPDATED_CREATEDAT).updatedat(UPDATED_UPDATEDAT).value(UPDATED_VALUE);

        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getCreatedat()).isEqualTo(UPDATED_CREATEDAT);
        assertThat(testTag.getUpdatedat()).isEqualTo(UPDATED_UPDATEDAT);
        assertThat(testTag.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();
        tag.setId(count.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        int databaseSizeBeforeDelete = tagRepository.findAll().size();

        // Delete the tag
        restTagMockMvc.perform(delete(ENTITY_API_URL_ID, tag.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
