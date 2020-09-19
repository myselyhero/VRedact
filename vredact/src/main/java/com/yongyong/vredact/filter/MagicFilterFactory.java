package com.yongyong.vredact.filter;

import com.yongyong.vredact.filter.advanced.MagicAmaroFilter;
import com.yongyong.vredact.filter.advanced.MagicAntiqueFilter;
import com.yongyong.vredact.filter.advanced.MagicBlackCatFilter;
import com.yongyong.vredact.filter.advanced.MagicBrannanFilter;
import com.yongyong.vredact.filter.advanced.MagicBrooklynFilter;
import com.yongyong.vredact.filter.advanced.MagicCalmFilter;
import com.yongyong.vredact.filter.advanced.MagicCoolFilter;
import com.yongyong.vredact.filter.advanced.MagicCrayonFilter;
import com.yongyong.vredact.filter.advanced.MagicEarlyBirdFilter;
import com.yongyong.vredact.filter.advanced.MagicEmeraldFilter;
import com.yongyong.vredact.filter.advanced.MagicEvergreenFilter;
import com.yongyong.vredact.filter.advanced.MagicFairytaleFilter;
import com.yongyong.vredact.filter.advanced.MagicFreudFilter;
import com.yongyong.vredact.filter.advanced.MagicHealthyFilter;
import com.yongyong.vredact.filter.advanced.MagicHefeFilter;
import com.yongyong.vredact.filter.advanced.MagicHudsonFilter;
import com.yongyong.vredact.filter.advanced.MagicImageAdjustFilter;
import com.yongyong.vredact.filter.advanced.MagicInkwellFilter;
import com.yongyong.vredact.filter.advanced.MagicKevinFilter;
import com.yongyong.vredact.filter.advanced.MagicLatteFilter;
import com.yongyong.vredact.filter.advanced.MagicLomoFilter;
import com.yongyong.vredact.filter.advanced.MagicN1977Filter;
import com.yongyong.vredact.filter.advanced.MagicNashvilleFilter;
import com.yongyong.vredact.filter.advanced.MagicNostalgiaFilter;
import com.yongyong.vredact.filter.advanced.MagicPixarFilter;
import com.yongyong.vredact.filter.advanced.MagicRiseFilter;
import com.yongyong.vredact.filter.advanced.MagicRomanceFilter;
import com.yongyong.vredact.filter.advanced.MagicSakuraFilter;
import com.yongyong.vredact.filter.advanced.MagicSierraFilter;
import com.yongyong.vredact.filter.advanced.MagicSketchFilter;
import com.yongyong.vredact.filter.advanced.MagicSkinWhitenFilter;
import com.yongyong.vredact.filter.advanced.MagicSunriseFilter;
import com.yongyong.vredact.filter.advanced.MagicSunsetFilter;
import com.yongyong.vredact.filter.advanced.MagicSutroFilter;
import com.yongyong.vredact.filter.advanced.MagicSweetsFilter;
import com.yongyong.vredact.filter.advanced.MagicTenderFilter;
import com.yongyong.vredact.filter.advanced.MagicToasterFilter;
import com.yongyong.vredact.filter.advanced.MagicValenciaFilter;
import com.yongyong.vredact.filter.advanced.MagicWaldenFilter;
import com.yongyong.vredact.filter.advanced.MagicWarmFilter;
import com.yongyong.vredact.filter.advanced.MagicWhiteCatFilter;
import com.yongyong.vredact.filter.advanced.MagicXproIIFilter;
import com.yongyong.vredact.filter.gpuimage.GPUImageBrightnessFilter;
import com.yongyong.vredact.filter.gpuimage.GPUImageContrastFilter;
import com.yongyong.vredact.filter.gpuimage.GPUImageExposureFilter;
import com.yongyong.vredact.filter.gpuimage.GPUImageFilter;
import com.yongyong.vredact.filter.gpuimage.GPUImageHueFilter;
import com.yongyong.vredact.filter.gpuimage.GPUImageSaturationFilter;
import com.yongyong.vredact.filter.gpuimage.GPUImageSharpenFilter;

public class MagicFilterFactory{
	
	private static MagicFilterType filterType = MagicFilterType.NONE;
	
	public static GPUImageFilter initFilters(MagicFilterType type){
		filterType = type;
		switch (type) {
		case WHITECAT:
			return new MagicWhiteCatFilter();
		case BLACKCAT:
			return new MagicBlackCatFilter();
		case SKINWHITEN:
			return new MagicSkinWhitenFilter();
		case ROMANCE:
			return new MagicRomanceFilter();
		case SAKURA:
			return new MagicSakuraFilter();
		case AMARO:
			return new MagicAmaroFilter();
		case WALDEN:
			return new MagicWaldenFilter();
		case ANTIQUE:
			return new MagicAntiqueFilter();
		case CALM:
			return new MagicCalmFilter();
		case BRANNAN:
			return new MagicBrannanFilter();
		case BROOKLYN:
			return new MagicBrooklynFilter();
		case EARLYBIRD:
			return new MagicEarlyBirdFilter();
		case FREUD:
			return new MagicFreudFilter();
		case HEFE:
			return new MagicHefeFilter();
		case HUDSON:
			return new MagicHudsonFilter();
		case INKWELL:
			return new MagicInkwellFilter();
		case KEVIN:
			return new MagicKevinFilter();
		case LOMO:
			return new MagicLomoFilter();
		case N1977:
			return new MagicN1977Filter();
		case NASHVILLE:
			return new MagicNashvilleFilter();
		case PIXAR:
			return new MagicPixarFilter();
		case RISE:
			return new MagicRiseFilter();
		case SIERRA:
			return new MagicSierraFilter();
		case SUTRO:
			return new MagicSutroFilter();
		case TOASTER2:
			return new MagicToasterFilter();
		case VALENCIA:
			return new MagicValenciaFilter();
		case XPROII:
			return new MagicXproIIFilter();
		case EVERGREEN:
			return new MagicEvergreenFilter();
		case HEALTHY:
			return new MagicHealthyFilter();
		case COOL:
			return new MagicCoolFilter();
		case EMERALD:
			return new MagicEmeraldFilter();
		case LATTE:
			return new MagicLatteFilter();
		case WARM:
			return new MagicWarmFilter();
		case TENDER:
			return new MagicTenderFilter();
		case SWEETS:
			return new MagicSweetsFilter();
		case NOSTALGIA:
			return new MagicNostalgiaFilter();
		case FAIRYTALE:
			return new MagicFairytaleFilter();
		case SUNRISE:
			return new MagicSunriseFilter();
		case SUNSET:
			return new MagicSunsetFilter();
		case CRAYON:
			return new MagicCrayonFilter();
		case SKETCH:
			return new MagicSketchFilter();
		//image adjust
		case BRIGHTNESS:
			return new GPUImageBrightnessFilter();
		case CONTRAST:
			return new GPUImageContrastFilter();
		case EXPOSURE:
			return new GPUImageExposureFilter();
		case HUE:
			return new GPUImageHueFilter();
		case SATURATION:
			return new GPUImageSaturationFilter();
		case SHARPEN:
			return new GPUImageSharpenFilter();
		case IMAGE_ADJUST:
			return new MagicImageAdjustFilter();
		default:
			return null;
		}
	}
	
	public MagicFilterType getCurrentFilterType(){
		return filterType;
	}
}
