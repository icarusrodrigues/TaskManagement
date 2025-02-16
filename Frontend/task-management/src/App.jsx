import "primereact/resources/themes/arya-blue/theme.css";
import "primeflex/primeflex.css";
import "primeicons/primeicons.css";

import Paths from "./routes/Paths";
import { AuthContext } from "./contexts/AuthContext";

const App = () => {

  return (
    <>
      <AuthContext>
        <Paths/>
      </AuthContext>
    </>
  );
};
 
export default App;