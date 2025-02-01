.PHONY: windows-distribution

windows-distribution:
	rm -rf "app/build/tmp/jpackage" \
	&& jpackage \
		--name "MikuPush" \
		--copyright "© 2025 MikuPush. All rights reserved." \
		--icon "app/src/main/resources/icon.ico" \
		--vendor "MagonxESP" \
		--app-version "1.0.0" \
		--input "app/build/libs" \
		--main-jar "app-1.0-SNAPSHOT-all.jar" \
		--resource-dir "app/resources/windows" \
		--dest "app/build/dist" \
		--temp "app/build/tmp/jpackage" \
		--type msi \
		--verbose
