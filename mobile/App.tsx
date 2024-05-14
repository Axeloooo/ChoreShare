import { StyleSheet } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Provider } from "react-redux";
import Navigator from "./src/navigation/Navigator";
import store from "./src/redux/store";
import { createTables } from "./src/database";

createTables();

const App = (): React.JSX.Element => {
  return (
    <SafeAreaView style={styles.container}>
      <Provider store={store}>
        <Navigator></Navigator>
      </Provider>
    </SafeAreaView>
  );
};

export default App;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
