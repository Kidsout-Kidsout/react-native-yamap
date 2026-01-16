require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNYamap"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => min_ios_version_supported }
  s.source       = { :git => "https://dev.kidsout.ru.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,mm}"
  s.private_header_files = "ios/**/*.h"

  s.dependency "React"
  s.dependency "YandexMapsMobile", "4.29.0-full"

  s.xcconfig = {
    "OTHER_CPLUSPLUSFLAGS" => "$(inherited) -fcxx-modules -fmodules",
  }

  install_modules_dependencies(s)
end
