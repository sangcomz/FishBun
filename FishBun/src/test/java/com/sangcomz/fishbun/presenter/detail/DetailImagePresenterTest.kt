package com.sangcomz.fishbun.presenter.detail

import android.net.Uri
import com.sangcomz.fishbun.adapter.image.ImageAdapter
import com.sangcomz.fishbun.ui.detail.DetailImageContract
import com.sangcomz.fishbun.ui.detail.model.DetailImageRepository
import com.sangcomz.fishbun.ui.detail.mvp.DetailImagePresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.verification.VerificationMode


@RunWith(MockitoJUnitRunner::class)
class DetailImagePresenterTest {

    @Mock
    lateinit var view: DetailImageContract.View

    @Mock
    lateinit var repository: DetailImageRepository

    private lateinit var presenter: DetailImagePresenter

    @Before
    fun initialize() {
        presenter = DetailImagePresenter(view, repository)
    }

    @Test
    fun `test onCountClick getPickerImage() return null`() {
        `when`(repository.getPickerImage(10)).thenReturn(null)

        presenter.onCountClick(10)

        verify(repository, never()).unselectImage(Uri.EMPTY)
        verify(repository, never()).isFullSelected()
        verify(repository, never()).selectImage(Uri.EMPTY)
        verify(repository, never()).getMessageLimitReached()
        verify(repository, never()).checkForFinish()
        verify(repository, never()).getImageIndex(Uri.EMPTY)
        verify(repository, never()).getMaxCount()
        verify(view, never()).finishActivity()
        verify(view, never()).showSnackbar("")
        verify(view, never()).unselectImage()
        verify(view, never()).updateRadioButtonWithDrawable()
        verify(view, never()).updateRadioButtonWithText("")
    }

    @Test
    fun `test onCountClick unselected case`() {
        `when`(repository.getPickerImage(10)).thenReturn(Uri.parse("uriString"))

        val pickerImage = repository.getPickerImage(10)!!

        `when`(repository.isSelected(pickerImage)).thenReturn(true)
        `when`(repository.getImageIndex(pickerImage)).thenReturn(-1)

        presenter.onCountClick(10)

        verify(repository).isSelected(pickerImage)
        verify(repository).unselectImage(pickerImage)
        verify(repository, never()).isFullSelected()
        verify(repository, never()).selectImage(pickerImage)
        verify(repository, never()).getMessageLimitReached()
        verify(repository, never()).checkForFinish()
        verify(repository).getImageIndex(pickerImage)
        verify(repository, never()).getMaxCount()
        verify(view, never()).finishActivity()
        verify(view, never()).showSnackbar(repository.getMessageLimitReached())
        verify(view).unselectImage()
        verify(view, never()).updateRadioButtonWithDrawable()
        verify(view, never()).updateRadioButtonWithText("${repository.getImageIndex(pickerImage) + 1}")
    }

    @Test
    fun `test onCountClick selected but isFullSelected case`() {
        `when`(repository.getPickerImage(10)).thenReturn(Uri.parse("uriString"))

        val pickerImage = repository.getPickerImage(10)!!

        `when`(repository.isSelected(pickerImage)).thenReturn(false)
        `when`(repository.isFullSelected()).thenReturn(true)
        `when`(repository.getImageIndex(pickerImage)).thenReturn(1)

        presenter.onCountClick(10)

        verify(repository).isSelected(pickerImage)
        verify(repository, never()).unselectImage(pickerImage)
        verify(repository).isFullSelected()
        verify(repository, never()).selectImage(pickerImage)
        verify(repository).getMessageLimitReached()
        verify(repository, never()).checkForFinish()
        verify(repository).getImageIndex(pickerImage)
        verify(repository).getMaxCount()
        verify(view, never()).finishActivity()
        verify(view).showSnackbar(repository.getMessageLimitReached())
        verify(view, never()).unselectImage()
        verify(view, never()).updateRadioButtonWithDrawable()
        verify(view).updateRadioButtonWithText("${repository.getImageIndex(pickerImage) + 1}")
    }

    @Test
    fun `test onCountClick select and not auto finish and maxCount == 1 case`() {
        `when`(repository.getPickerImage(10)).thenReturn(Uri.parse("uriString"))

        val pickerImage = repository.getPickerImage(10)!!

        `when`(repository.isSelected(pickerImage)).thenReturn(false)
        `when`(repository.isFullSelected()).thenReturn(false)
        `when`(repository.getImageIndex(pickerImage)).thenReturn(1)
        `when`(repository.getMaxCount()).thenReturn(1)

        presenter.onCountClick(10)

        verify(repository).isSelected(pickerImage)
        verify(repository, never()).unselectImage(pickerImage)
        verify(repository).isFullSelected()
        verify(repository).selectImage(pickerImage)
        verify(repository, never()).getMessageLimitReached()
        verify(repository).checkForFinish()
        verify(repository).getMaxCount()
        verify(view, never()).finishActivity()
        verify(view, never()).showSnackbar(repository.getMessageLimitReached())
        verify(view, never()).unselectImage()
        verify(view).updateRadioButtonWithDrawable()
        verify(
            view,
            never()
        ).updateRadioButtonWithText("${repository.getImageIndex(pickerImage) + 1}")
    }

    @Test
    fun `test onCountClick select and not auto finish and maxCount != 1 case`() {
        `when`(repository.getPickerImage(10)).thenReturn(Uri.parse("uriString"))

        val pickerImage = repository.getPickerImage(10)!!

        `when`(repository.isSelected(pickerImage)).thenReturn(false)
        `when`(repository.isFullSelected()).thenReturn(false)
        `when`(repository.getImageIndex(pickerImage)).thenReturn(1)
        `when`(repository.getMaxCount()).thenReturn(2)
        `when`(repository.checkForFinish()).thenReturn(false)

        presenter.onCountClick(10)

        verify(repository).isSelected(pickerImage)
        verify(repository, never()).unselectImage(pickerImage)
        verify(repository).isFullSelected()
        verify(repository).selectImage(pickerImage)
        verify(repository, never()).getMessageLimitReached()
        verify(repository).checkForFinish()
        verify(repository).getMaxCount()
        verify(view, never()).finishActivity()
        verify(view, never()).showSnackbar(repository.getMessageLimitReached())
        verify(view, never()).unselectImage()
        verify(view, never()).updateRadioButtonWithDrawable()
        verify(view).updateRadioButtonWithText("${repository.getImageIndex(pickerImage) + 1}")
    }

    @Test
    fun `test onCountClick select and auto finish and maxCount == 1 case`() {
        `when`(repository.getPickerImage(10)).thenReturn(Uri.parse("uriString"))

        val pickerImage = repository.getPickerImage(10)!!

        `when`(repository.isSelected(pickerImage)).thenReturn(false)
        `when`(repository.isFullSelected()).thenReturn(false)
        `when`(repository.getImageIndex(pickerImage)).thenReturn(1)
        `when`(repository.getMaxCount()).thenReturn(1)
        `when`(repository.checkForFinish()).thenReturn(true)

        presenter.onCountClick(10)

        verify(repository).isSelected(pickerImage)
        verify(repository, never()).unselectImage(pickerImage)
        verify(repository).isFullSelected()
        verify(repository).selectImage(pickerImage)
        verify(repository, never()).getMessageLimitReached()
        verify(repository).checkForFinish()
        verify(repository).getMaxCount()
        verify(view).finishActivity()
        verify(view, never()).showSnackbar(repository.getMessageLimitReached())
        verify(view, never()).unselectImage()
        verify(view).updateRadioButtonWithDrawable()
        verify(view, never()).updateRadioButtonWithText("${repository.getImageIndex(pickerImage) + 1}")
    }

    @Test
    fun `test onCountClick select and auto finish and maxCount != 1 case`() {
        `when`(repository.getPickerImage(10)).thenReturn(Uri.parse("uriString"))

        val pickerImage = repository.getPickerImage(10)!!

        `when`(repository.isSelected(pickerImage)).thenReturn(false)
        `when`(repository.isFullSelected()).thenReturn(false)
        `when`(repository.getImageIndex(pickerImage)).thenReturn(1)
        `when`(repository.getMaxCount()).thenReturn(2)
        `when`(repository.checkForFinish()).thenReturn(true)

        presenter.onCountClick(10)

        verify(repository).isSelected(pickerImage)
        verify(repository, never()).unselectImage(pickerImage)
        verify(repository).isFullSelected()
        verify(repository).selectImage(pickerImage)
        verify(repository, never()).getMessageLimitReached()
        verify(repository).checkForFinish()
        verify(repository).getMaxCount()
        verify(view).finishActivity()
        verify(view, never()).showSnackbar(repository.getMessageLimitReached())
        verify(view, never()).unselectImage()
        verify(view, never()).updateRadioButtonWithDrawable()
        verify(view).updateRadioButtonWithText("${repository.getImageIndex(pickerImage) + 1}")
    }
}