package ru.kidsout.yamap

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager
import java.util.HashMap

class YamapPackage : BaseReactPackage() {
  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return listOf(
      YamapViewManager(reactContext),
      YamapCircleViewManager(reactContext),
      YamapPolygonViewManager(reactContext),
      YamapMarkerViewManager(reactContext),
      YamapClustersViewManager(reactContext)
    )
  }

  override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
    return when (name) {
      YamapModule.NAME -> YamapModule(reactContext)
      YamapGeocodeModule.NAME -> YamapGeocodeModule(reactContext)
      YamapSuggestsModule.NAME -> YamapSuggestsModule(reactContext)
      YamapViewManager.NAME -> YamapViewManager(reactContext)
      YamapCircleViewManager.NAME -> YamapCircleViewManager(reactContext)
      YamapPolygonViewManager.NAME -> YamapPolygonViewManager(reactContext)
      YamapMarkerViewManager.NAME -> YamapMarkerViewManager(reactContext)
      YamapClustersViewManager.NAME -> YamapClustersViewManager(reactContext)
      else -> null
    }
  }

  override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
    val moduleList: Array<Class<out NativeModule?>> =
      arrayOf(
        YamapModule::class.java,
        YamapGeocodeModule::class.java,
        YamapSuggestsModule::class.java,
        YamapViewManager::class.java,
        YamapCircleViewManager::class.java,
        YamapPolygonViewManager::class.java,
        YamapMarkerViewManager::class.java,
        YamapClustersViewManager::class.java
        )
    val reactModuleInfoMap: MutableMap<String, ReactModuleInfo> = HashMap()
    for (moduleClass in moduleList) {
      val reactModule = moduleClass.getAnnotation(ReactModule::class.java) ?: throw Exception("Cannot get react annotation for ${moduleClass.name}")
      reactModuleInfoMap[reactModule.name] =
        ReactModuleInfo(
          reactModule.name,
          moduleClass.name,
          reactModule.canOverrideExistingModule,
          reactModule.needsEagerInit,
          reactModule.isCxxModule,
          true
        )
    }
    return ReactModuleInfoProvider {
      reactModuleInfoMap
    }
  }
}
